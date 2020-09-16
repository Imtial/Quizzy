package com.example.quizzy.quizgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.quizzy.NavGraphViewModelFactory
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentGameResultBinding

class GameResultFragment: Fragment() {
    private val TAG = "QUIZ-GAME"

    private val gameViewModel: GameViewModel by navGraphViewModels(R.id.game_navigation_graph) {
        NavGraphViewModelFactory(requireActivity().application, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = gameViewModel

        binding.buttonSubmit.setOnClickListener {button ->
            gameViewModel.submit(binding.ratingBar.rating)
            button.findNavController().navigate(GameResultFragmentDirections.actionGameResultFragmentToHomeFragment())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideTopTextView()
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)
    }
}