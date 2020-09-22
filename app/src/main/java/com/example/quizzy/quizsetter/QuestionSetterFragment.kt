package com.example.quizzy.quizsetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.quizzy.OnButtonClickListener
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.domain.MCQ
import com.example.quizzy.domain.Question
import com.example.quizzy.domain.SINGLE
import com.example.quizzy.domain.TEXT
import com.example.quizzy.databinding.FragmentQuestionSetterBinding
import java.lang.IllegalArgumentException

const val MAX_OPTIONS = 10

class QuestionSetterFragment: Fragment() {
    private val TAG = "SETTING-QUESTION"

    private var optionViewList = mutableListOf<View>()
    private var currentQuestionIndex = 0

    private val onClickListener = View.OnClickListener {it: View ->
        val checkButton = it as RadioButton
        for (view in optionViewList) {
            val radioButton = view.findViewById<RadioButton>(R.id.radio_button)
            if (radioButton != checkButton && radioButton.isChecked) radioButton.isChecked = false
        }
    }
    private val viewModel: QuizSetterViewModel by navGraphViewModels(R.id.quiz_setter_navigation_graph) {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentActivity = requireActivity() as QuizGameActivity
        val binding = FragmentQuestionSetterBinding.inflate(inflater, container, false)

        binding.questionType.setOnCheckedChangeListener { radioGroup, checkButtonId ->
            viewModel.setQuestionType(checkButtonId)
            updateOptionViews(binding.optionsContainer, checkButtonId)
        }

        binding.buttonAddOption.setOnClickListener {
            viewModel.questionType.value?.let { currentViewId ->
                if (optionViewList.size <= MAX_OPTIONS) {
                    addOptionView(binding.optionsContainer, currentViewId)
                }
            }
        }

        parentActivity.setOnButtonClickListener(object : OnButtonClickListener {

            override fun nextButtonClicked() {
                val question = viewModel.questionType.value?.let { typeViewId -> extractQuestion(binding, typeViewId) }
                if (question == null) Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
                else {
                    viewModel.setQuestion(currentQuestionIndex, question)
                    currentQuestionIndex++
                    if (viewModel.questionList?.size!! > currentQuestionIndex) createViewFromData(binding, viewModel.questionList!![currentQuestionIndex])
                    else resetBinding(binding)
                    parentActivity.setQuestionNumberOnTopBar((currentQuestionIndex+1).toString())
                }
            }

            override fun completeButtonClicked() {
                val question = viewModel.questionType.value?.let { typeViewId -> extractQuestion(binding, typeViewId) }
                if (question == null) Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
                else {
                    viewModel.setQuestion(currentQuestionIndex, question)
                }
                findNavController().navigate(QuestionSetterFragmentDirections.actionQuestionSetterFragmentToDecisionSetterFragment())
            }

            override fun backButtonClicked() {
                if (currentQuestionIndex > 0) {
                    val question = viewModel.questionType.value?.let { typeViewId -> extractQuestion(binding, typeViewId) }
                    if (question == null) binding.question.error = null
                    question?.let { viewModel.setQuestion(currentQuestionIndex, it) }
                    currentQuestionIndex--
                    createViewFromData(binding, viewModel.questionList!![currentQuestionIndex])
                    parentActivity.setQuestionNumberOnTopBar((currentQuestionIndex+1).toString())
                }
            }
        })

        return binding.root
    }

    private fun resetBinding(binding: FragmentQuestionSetterBinding) {
        binding.question.text.clear()
        optionViewList.clear()
        binding.optionsContainer.removeAllViews()
        binding.questionMarks.text.clear()
    }

    private fun addOptionView (group: LinearLayout, viewTypeId: Int) {
        when(viewTypeId) {
            R.id.radio_single -> createOptionView(group, R.layout.option_radio_button)
            R.id.radio_multiple -> createOptionView(group, R.layout.option_checkbox)
            R.id.radio_typed -> createOptionView(group, R.layout.option_textbox)
        }
    }

    private fun createOptionView(group: ViewGroup, layoutId: Int, text: String? = null, viewList: MutableList<View> = optionViewList){
        val optionView = LayoutInflater.from(this.context).inflate(layoutId, group, false)
        val buttonDelete = optionView.findViewById<ImageView>(R.id.option_delete)
        val optionText = optionView.findViewById<EditText>(R.id.option_text)
        text?.apply {
            optionText.setText(text)
        }
        optionText.requestFocus()

        when(layoutId) {
            R.layout.option_radio_button -> {
                val radioButton = optionView.findViewById<RadioButton>(R.id.radio_button)
                radioButton.setOnClickListener (onClickListener)
            }
        }

        viewList.add(optionView)
        group.addView(optionView)

        buttonDelete.setOnClickListener {
            group.removeView(optionView)
            viewList.remove(optionView)
        }
    }

    private fun updateOptionViews(group: LinearLayout, optionTypeId: Int) {
        var layoutId = R.layout.option_radio_button
        when (optionTypeId) {
            R.id.radio_single   -> layoutId = R.layout.option_radio_button
            R.id.radio_multiple -> layoutId = R.layout.option_checkbox
            R.id.radio_typed    -> layoutId = R.layout.option_textbox
        }
        group.removeAllViews()
        val newOptionViewList = mutableListOf<View>()
        for (optionView in optionViewList) {
            val text = optionView.findViewById<EditText>(R.id.option_text).text
            createOptionView(group, layoutId, text.toString(), newOptionViewList)
        }
        optionViewList = newOptionViewList
    }

    private fun createViewFromData(binding: FragmentQuestionSetterBinding, question: Question) {
        resetBinding(binding)
        val typeViewId: Int
        val optionLayout: Int
        when(question.type) {
            SINGLE -> {
                typeViewId = R.id.radio_single
                optionLayout = R.layout.option_radio_button
            }
            MCQ -> {
                typeViewId = R.id.radio_multiple
                optionLayout = R.layout.option_checkbox
            }
            TEXT -> {
                typeViewId = R.id.radio_typed
                optionLayout = R.layout.option_textbox
            }
            else -> throw IllegalArgumentException("Unknown question type id for question type")
        }


        binding.questionType.check(typeViewId)
        binding.question.setText(question.description)
        binding.questionMarks.setText(question.marks.toString())
        var answerIndex = 0
        for (option in question.options!!) {
            createOptionView(binding.optionsContainer, optionLayout, option)
            if (answerIndex <= question.answers?.lastIndex!! && question.answers!![answerIndex].equals(option, true)) {
                when(optionLayout) {
                    R.layout.option_radio_button -> optionViewList[optionViewList.lastIndex].findViewById<RadioButton>(R.id.radio_button).isChecked = true
                    R.layout.option_checkbox -> optionViewList[optionViewList.lastIndex].findViewById<CheckBox>(R.id.checkbox).isChecked = true
                }
                answerIndex++
            }
        }
    }

    private fun extractQuestion(binding: FragmentQuestionSetterBinding, typeViewId: Int): Question? {
        if (binding.question.text.isNullOrBlank()) {
            binding.question.error = "You must provide a question!"
            return null
        }
        if (typeViewId != R.id.radio_typed && optionViewList.size < 2) {
            Toast.makeText(requireContext(), "Must provide multiple options!", Toast.LENGTH_SHORT).show()
            return null
        }

        val description = binding.question.text.toString()
        val type = when(typeViewId) {
            R.id.radio_single -> SINGLE
            R.id.radio_multiple -> MCQ
            R.id.radio_typed -> TEXT
            else -> throw IllegalStateException("Unknown Radio Button for question type")
        }
        val options = mutableListOf<String>()
        val answers = mutableListOf<String>()

        for (view in optionViewList) {
            val option: String = view.findViewById<EditText>(R.id.option_text).text.toString()
            options.add(option)

            val isChecked: Boolean = when (typeViewId) {
                R.id.radio_single -> view.findViewById<RadioButton>(R.id.radio_button).isChecked
                R.id.radio_multiple -> view.findViewById<CheckBox>(R.id.checkbox).isChecked
                else -> false
            }
            if (isChecked) answers.add(option)
        }
        val marks = if (binding.questionMarks.text.isNullOrBlank()) binding.questionMarks.hint.toString().toFloat()
            else binding.questionMarks.text.toString().toFloat()

        return Question(description, type, options, marks, answers)
    }

    override fun onStart() {
        super.onStart()
        val parentActivity = (requireActivity() as QuizGameActivity)
        parentActivity.setQuestionNumberOnTopBar((currentQuestionIndex+1).toString())
        parentActivity.showButton(R.id.button_back, R.id.button_next, R.id.button_complete)
        parentActivity.showTopTextView()
        parentActivity.supportActionBar?.hide()
    }
}