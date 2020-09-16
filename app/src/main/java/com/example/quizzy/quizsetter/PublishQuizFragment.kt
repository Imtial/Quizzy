package com.example.quizzy.quizsetter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.domain.Quiz
import com.example.quizzy.databinding.FragmentPublishQuizBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PublishQuizFragment: Fragment() {

    private val quiz = Quiz()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)

        val binding = FragmentPublishQuizBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
                .get(PublishQuizViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val tagListAdapter = TagListAdapter(requireContext())
        binding.tagList.adapter = tagListAdapter
        val layoutManager = FlexboxLayoutManager(requireActivity().applicationContext)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        binding.tagList.layoutManager = layoutManager

        viewModel.tagList.observe(viewLifecycleOwner, {
            tagListAdapter.submitList(it)
            tagListAdapter.notifyDataSetChanged()
        })

        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            binding.buttonStartTime.text = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(calendar.time)
        }
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val timePickerDialog = TimePickerDialog(requireContext(), timeSetListener, hourOfDay, minute, false)
            timePickerDialog.show()
        }
        binding.buttonStartTime.setOnClickListener {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)
            datePickerDialog.show()
        }

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 180

        binding.buttonPublish.setOnClickListener {button ->
            // Populate Quiz with data from views
            extractDataFromViews(binding, calendar)
            viewModel.insert(quiz) // TODO - BUG here
//            parentActivity.finish()
            button.findNavController().navigate(PublishQuizFragmentDirections.actionPublishQuizFragmentToHomeFragment())
        }

        binding.buttonAddTags.setOnClickListener {
            val tag = binding.tagInput.text.toString().trim()
            if (tag.isNotEmpty()) {
                viewModel.addTag(tag)
            }
            binding.tagInput.text.clear()
        }

        binding.radioPrivate.setOnClickListener { binding.radioPublic.isChecked = false }
        binding.radioPublic.setOnClickListener { binding.radioPrivate.isChecked = false }

        return binding.root
    }

    private fun extractDataFromViews(binding: FragmentPublishQuizBinding, calendar: Calendar) {
        if (binding.quizTitle.text.isNotBlank()) quiz.title = binding.quizTitle.text.toString().trim()
        quiz.startTime = calendar.timeInMillis
        quiz.duration = binding.minutePicker.value
        if (binding.radioPrivate.isChecked) {
            if (binding.passwordInput.text.isNullOrBlank()) binding.passwordInput.error = "Private Question must have password"
            else quiz.password = binding.passwordInput.text.toString().trim()
        }
    }

}