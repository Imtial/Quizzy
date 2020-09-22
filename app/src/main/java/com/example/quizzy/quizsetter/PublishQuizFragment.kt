package com.example.quizzy.quizsetter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.databinding.FragmentPublishQuizBinding
import com.example.quizzy.domain.Quiz
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PublishQuizFragment: Fragment() {

    private val quiz = Quiz()
    private val viewModel: QuizSetterViewModel by navGraphViewModels(R.id.navigation) {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)

        val binding = FragmentPublishQuizBinding.inflate(inflater, container, false)

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
            var buttonText = "Pick time in future!!!"
            if (calendar.timeInMillis > System.currentTimeMillis() + 10*60000) {
                buttonText = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                        .format(calendar.time)
            }
            binding.buttonStartTime.text = buttonText
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
            viewModel.insert(quiz)
//            parentActivity.finish()
            button.findNavController().navigate(PublishQuizFragmentDirections.actionPublishQuizFragmentToHomeFragment())
        }

        binding.buttonAddTags.setOnClickListener {
            val tags = binding.tagInput.text.toString().trim().toLowerCase(Locale.ENGLISH).split(",")
            if (tags.isNotEmpty()) {
                viewModel.addTag(tags)
            }
            binding.tagInput.text.clear()
        }

        binding.radioPublic.isChecked = true
        binding.radioPrivate.setOnClickListener { binding.radioPublic.isChecked = false }
        binding.radioPublic.setOnClickListener { binding.radioPrivate.isChecked = false }

        return binding.root
    }

    private fun extractDataFromViews(binding: FragmentPublishQuizBinding, calendar: Calendar) {
        if (binding.quizTitle.text.isNotBlank()) quiz.title = binding.quizTitle.text.toString().trim()
        quiz.startTime = if (calendar.timeInMillis > System.currentTimeMillis() + 10 * 60000) calendar.timeInMillis / 1000 else null
        quiz.duration = binding.minutePicker.value.toDouble()
        if (binding.radioPrivate.isChecked) {
            if (binding.passwordInput.text.isNullOrBlank()) binding.passwordInput.error = "Private Question must have password"
            else quiz.password = binding.passwordInput.text.toString().trim()
        }
    }

}