package com.example.quizzy.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
                .get(ProfileViewModel::class.java)

        viewModel.editEnabled.observe(viewLifecycleOwner, {
            when(it) {
                true -> {
                    binding.buttonEdit.setImageResource(R.drawable.ic_confirm)
                    binding.userName.isEnabled = true
                    binding.userName.setSelection(binding.userName.text.length)
                    binding.userEmail.isEnabled = true
                }
                false -> {
                    binding.buttonEdit.setImageResource(R.drawable.ic_edit)
                    binding.userName.isEnabled = false
                    binding.userEmail.isEnabled = false
                }
            }
        })

        binding.buttonEdit.setOnClickListener {
            if (viewModel.editEnabled.value == null || viewModel.editEnabled.value == false)
                viewModel.enableEdit()
            else
                viewModel.disableEdit()
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