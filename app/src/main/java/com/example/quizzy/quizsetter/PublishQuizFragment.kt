package com.example.quizzy.quizsetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizzy.databinding.FragmentPublishQuizBinding

class PublishQuizFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentPublishQuizBinding.inflate(inflater, container, false)

        binding.buttonPublish.setOnClickListener {
            requireActivity().finish()
        }

        return binding.root
    }
}