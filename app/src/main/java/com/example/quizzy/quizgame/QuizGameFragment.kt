package com.example.quizzy.quizgame

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.quizzy.OnButtonClickListener
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.databinding.FragmentQuizGameBinding

class QuizGameFragment: Fragment() {
    private val TAG = "QUIZ-GAME"

    // TODO - Currently using root navigation graph. After merging change it to nested navigation graph
    private val gameViewModel: GameViewModel by navGraphViewModels(R.id.game_navigation_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentQuizGameBinding.inflate(inflater, container, false)
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.setOnButtonClickListener(object : OnButtonClickListener {
            override fun nextButtonClicked() {
                findNavController().navigate(QuizGameFragmentDirections.actionQuizGameFragmentToGameResultFragment())
            }

            override fun completeButtonClicked() {}

            override fun backButtonClicked() {}
        })

        gameViewModel.test = "DATA"
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.showButton(R.id.button_back, R.id.button_next)
        parentActivity.hideButton(R.id.button_complete)
        parentActivity.showTopTextView()
        parentActivity.setTextOnTopBar("Quiz Title")
    }
}