package com.example.quizzy.quizgame

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.quizzy.*
import com.example.quizzy.domain.MCQ
import com.example.quizzy.domain.Question
import com.example.quizzy.domain.SINGLE
import com.example.quizzy.domain.TEXT
import com.example.quizzy.databinding.FragmentQuizGameBinding
import com.example.quizzy.network.Status

class QuizGameFragment: Fragment() {
    private val TAG = "QUIZ-GAME"

    private val args: QuizGameFragmentArgs by navArgs()
    private val gameViewModel: GameViewModel by navGraphViewModels(R.id.game_navigation_graph) {
        NavGraphViewModelFactory(requireActivity().application, args.quizId)
    }
    private var type = SINGLE
    private var enabled = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentQuizGameBinding.inflate(inflater, container, false)
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.supportActionBar?.hide()
        parentActivity.setOnButtonClickListener(object : OnButtonClickListener {
            override fun nextButtonClicked() {
                submitChoices(binding)
                gameViewModel.triggerNextQuestion()
            }
            override fun completeButtonClicked() {
                gameViewModel.disableQuiz()
                binding.resultLoadingBar.visibility = View.VISIBLE
                gameViewModel.fetchAnswers()
            }
            override fun backButtonClicked() {
                submitChoices(binding)
                gameViewModel.triggerPrevQuestion()
            }
        })

        gameViewModel.answerResponse.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.resultLoadingBar.visibility = View.GONE
                gameViewModel.calculateResult(it)
                findNavController().navigate(QuizGameFragmentDirections.actionQuizGameFragmentToGameResultFragment())
            }
        })

        gameViewModel.submissionStatus.observe(viewLifecycleOwner, {
            it?.let {
                if (it == Status.FAILURE) {
                    binding.resultLoadingBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Submission failed", Toast.LENGTH_LONG).show()
                }
            }
        })

        gameViewModel.enabled.observe(viewLifecycleOwner, {
            if (it != null) enabled = it
        })

        gameViewModel.quiz.observe(viewLifecycleOwner, { quiz ->
            Log.i(TAG, "onCreateView: $quiz")
            if (quiz != null) {
                parentActivity.setTextOnTopBar(quiz.title)
                gameViewModel.init()
                if (quiz.duration == 0) binding.gameTimer.visibility = View.GONE
                else {
                    object : CountDownTimer(quiz.duration * 60000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val min = (millisUntilFinished / (60000)).toString()
                            val sec = String.format("%02d", (millisUntilFinished / 1000) % 60)
                            val time = "$min:$sec"
                            binding.gameTimer.text = time
                        }

                        override fun onFinish() {
                            val message = "Quiz Locked\nTimes Up!!!"
                            binding.gameTimer.text = message
                            binding.gameTimer.setTextColor(Color.RED)
                            submitChoices(binding)
                            gameViewModel.disableQuiz()
                        }
                    }.start()
                }
            } else {
                Log.i(TAG, "onCreateView: NULL Quiz object")
            }
        })

        gameViewModel.question.observe(viewLifecycleOwner, {
            type = it.type!!
            val questionCounter = "${gameViewModel.questionSerial+1}/${gameViewModel.totalQuestions}"
            binding.questionCounter.text = questionCounter
            val unanswered = "Unanswered\n${gameViewModel.totalQuestions - gameViewModel.answerMap.size}"
            binding.unansweredCounter.text = unanswered
            populateLayout(binding, it)
        })

        gameViewModel.lastQuestion.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    parentActivity.showButton(R.id.button_complete)
                }
                false -> {
                    parentActivity.hideButton(R.id.button_complete)
                }
            }
        })

        return binding.root
    }

    private fun submitChoices(binding: FragmentQuizGameBinding) {
        val choices = mutableListOf<String>()
        when(type) {
            TEXT -> {
                val answer = binding.answerText.text.toString().trim()
                if (answer.isNotBlank()) choices.add(answer)
                else return
            }
            else -> {
                for (view in optionViewList) {
                    if (view.isChecked) {
                        choices.add(view.text.toString())
                    }
                }
            }
        }
        if (choices.isNotEmpty()) gameViewModel.setChoices(choices)
    }

    override fun onStart() {
        super.onStart()

        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.showButton(R.id.button_back, R.id.button_next)
        parentActivity.hideButton(R.id.button_complete)
        parentActivity.showTopTextView()
        parentActivity.setTextOnTopBar("Quiz Title")
    }

    private val optionViewList = mutableListOf<CompoundButton>()

    private fun populateLayout(binding: FragmentQuizGameBinding, question: Question) {
        clearLayout(binding)
        val type = question.type
        var index = 0
        var id = 0

        binding.question.text = question.description
        when(type) {
            SINGLE -> {
                for (option in question.options!!) {
                    val radioButton = RadioButton(this.requireContext())
                    radioButton.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    radioButton.text = option
                    radioButton.id = ++id
                    radioButton.isEnabled = enabled
                    radioButton.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.quiz_option_selector, null))
                    if (!gameViewModel.getChoices().isNullOrEmpty()
                            && index < gameViewModel.getChoices()!!.size
                            && option == gameViewModel.getChoices()!![index]) {
                        radioButton.isChecked = true
                        index++
                    }
                    binding.answerRadioGroup.addView(radioButton)
                    optionViewList.add(radioButton)
                }
            }
            MCQ -> {
                for (option in question.options!!) {
                    val checkBox = CheckBox(this.requireContext())
                    checkBox.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    checkBox.text = option
                    checkBox.isEnabled = enabled
                    checkBox.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.quiz_option_selector, null))
                    if (!gameViewModel.getChoices().isNullOrEmpty()
                            && index < gameViewModel.getChoices()!!.size
                            && option == gameViewModel.getChoices()!![index]) {
                        checkBox.isChecked = true
                        index++
                    }
                    binding.answerRadioGroup.addView(checkBox)
                    optionViewList.add(checkBox)
                }
            }
            TEXT -> {
                if (!gameViewModel.getChoices().isNullOrEmpty()) {
                    binding.answerText.setText(gameViewModel.getChoices()!![index])
                }
                binding.answerText.visibility = View.VISIBLE
                binding.answerText.isEnabled = enabled
            }
        }
    }

    private fun clearLayout(binding: FragmentQuizGameBinding) {
        binding.answerText.visibility = View.GONE
        optionViewList.clear()
        binding.answerRadioGroup.removeAllViews()
    }
}