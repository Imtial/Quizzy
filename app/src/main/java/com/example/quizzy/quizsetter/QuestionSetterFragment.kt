package com.example.quizzy.quizsetter

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.quizzy.OnButtonClickListener
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.databinding.FragmentQuestionSetterBinding
import org.jetbrains.annotations.NotNull
import java.lang.IllegalArgumentException

const val MAX_OPTIONS = 10

class QuestionSetterFragment: Fragment() {
    private val TAG = "SETTING-QUESTION"

    private var optionViewList = mutableListOf<View>()
    private var currentQuestionNumber = 1

    private val onClickListener = View.OnClickListener {it: View ->
        val checkButton = it as RadioButton
        for (view in optionViewList) {
            val radioButton = view.findViewById<RadioButton>(R.id.radio_button)
            if (radioButton != checkButton && radioButton.isChecked) radioButton.isChecked = false
        }
    }
//    private val viewModel: QuestionSetterViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentQuestionSetterBinding.inflate(inflater, container, false)
        val viewModelFactory = ViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
                .get(QuestionSetterViewModel::class.java)
//        viewModel.setApplication(requireActivity().application)

        viewModel.question.observe(viewLifecycleOwner, { question ->
            Log.i(TAG, "Triggered")
            if (question != null) {
                Log.i(TAG, "Previous data exists")
//                createViewFromData(binding, question)
            }
        })

        try {
            val args = QuestionSetterFragmentArgs.fromBundle(requireArguments())
            currentQuestionNumber = args.questionNumber
            viewModel.setQuestionSerial(currentQuestionNumber)
        } catch (e: IllegalArgumentException) { }

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

        (requireActivity() as QuizGameActivity).setOnButtonClickListener(object : OnButtonClickListener {

            override fun nextButtonClicked() {
                val question = viewModel.questionType.value?.let { typeViewId -> extractQuestion(binding, typeViewId) }
                if (question == null) Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
                else {
                    viewModel.insert(question)
                    findNavController().navigate(QuestionSetterFragmentDirections.actionQuestionSetterFragmentSelf(currentQuestionNumber+1))
                }
            }

            override fun completeButtonClicked() {
                findNavController().navigate(QuestionSetterFragmentDirections.actionQuestionSetterFragmentToDecisionSetterFragment())
            }

            override fun backButtonClicked() {
                if (currentQuestionNumber > 1)
                    findNavController().navigate(QuestionSetterFragmentDirections.actionQuestionSetterFragmentSelf(currentQuestionNumber - 1))
            }
        })

        return binding.root
    }

    private fun addOptionView (group: LinearLayout, viewTypeId: Int) {
        when(viewTypeId) {
            R.id.radio_single -> createOptionView(group, R.layout.option_radio_button)
            R.id.radio_multiple -> createOptionView(group, R.layout.option_checkbox)
            R.id.radio_typed -> createOptionView(group, R.layout.option_textbox)
        }
    }

    private fun createOptionView(group: ViewGroup, layoutId: Int, text: Editable? = null, viewList: MutableList<View> = optionViewList){
        val optionView = LayoutInflater.from(this.context).inflate(layoutId, group, false)
        val buttonDelete = optionView.findViewById<ImageView>(R.id.option_delete)
        val optionText = optionView.findViewById<EditText>(R.id.option_text)
        text?.apply {
            optionText.text = text
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
            createOptionView(group, layoutId, text, newOptionViewList)
        }
        optionViewList = newOptionViewList
    }

    private fun createViewFromData(binding: FragmentQuestionSetterBinding, question: Question) {
        val typeViewId = when(question.type) {
            SINGLE -> R.id.radio_single
            MULTIPLE -> R.id.radio_multiple
            TYPED -> R.id.radio_typed
            else -> throw IllegalArgumentException("Unknown RadioButton id for question type")
        }
        binding.questionType.check(typeViewId)
        binding.question.setText(question.description)
    }

    private fun extractQuestion(binding: FragmentQuestionSetterBinding, typeViewId: Int): Question? {
        if (binding.question.text.isNullOrBlank()) {
            binding.question.error = "You must provide a question!"
            return null
        }
        if (typeViewId != R.id.radio_typed && optionViewList.size < 2) {
            optionViewList[0].findViewById<EditText>(R.id.option_text).error = "Must provide multiple options!"
            return null
        }

        val description = binding.question.text.toString()
        val type = when(typeViewId) {
            R.id.radio_single -> SINGLE
            R.id.radio_multiple -> MULTIPLE
            R.id.radio_typed -> TYPED
            else -> throw IllegalStateException("Unknown Radio Button for question type")
        }
        val options = mutableListOf<String>()
        val answers = mutableListOf<String>()

        for (view in optionViewList) {
            val option: String = view.findViewById<EditText>(R.id.option_text).text.toString()
            options.add(option)

            val isChecked: Boolean = when (typeViewId) {
                R.id.radio_single -> view.findViewById<RadioButton>(R.id.radio_button).isChecked
//                R.id.radio_multiple -> view.findViewById<CheckBox>(R.id.checkbox).isChecked
                else -> false
            }
            if (isChecked) answers.add(option)
        }
//        val marks = if (binding.questionMarks.text.isNullOrBlank()) binding.questionMarks.hint.toString()
//            else binding.questionMarks.text.toString()

//        return null
        return Question(currentQuestionNumber, description, type, options, /*marks.toFloat()*/ 1F, answers)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as QuizGameActivity).setText(currentQuestionNumber.toString())
    }
}