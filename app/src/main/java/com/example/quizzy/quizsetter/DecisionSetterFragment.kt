package com.example.quizzy.quizsetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.OnButtonClickListener
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory
import com.example.quizzy.domain.CachedResponse
import com.example.quizzy.databinding.FragmentDecisionSetterBinding

class DecisionSetterFragment: Fragment() {

    private val conditionViewList = mutableListOf<View>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentActivity = (requireActivity() as QuizGameActivity)
        parentActivity.setTextOnTopBar("Set Conditional Decision")
        parentActivity.hideButton(R.id.button_back, R.id.button_next)

        val binding = FragmentDecisionSetterBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
                .get(DecisionSetterViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        parentActivity.setOnButtonClickListener(object : OnButtonClickListener {
            override fun nextButtonClicked() {
                TODO("Not yet implemented")
            }

            override fun completeButtonClicked() {
                val responseList = extractCondition()
                viewModel.insert(*responseList.toTypedArray())
                findNavController().navigate(DecisionSetterFragmentDirections.actionDecisionSetterFragmentToPublishQuizFragment())
            }

            override fun backButtonClicked() {
                TODO("Not yet implemented")
            }
        })

        binding.buttonAddCondition.setOnClickListener {
            if(conditionViewList.isNullOrEmpty() || !isConditionBlank(conditionViewList[conditionViewList.lastIndex])) {
                addConditionView(binding.conditionContainer)
            }
        }

        return binding.root
    }

    private fun isConditionBlank(view: View) : Boolean{
        var isBlank = false
        val etLow = view.findViewById<EditText>(R.id.low_value)
        val etHigh = view.findViewById<EditText>(R.id.high_value)
        val etMessage = view.findViewById<EditText>(R.id.decision_message)
        if (etLow.text.isNullOrBlank()) {
            isBlank = true
            etLow.error = "You must set a Low value!"
        }
        if (etHigh.text.isNullOrBlank()) {
            isBlank = true
            etLow.error = "You must set a High value!"
        }
        if (etMessage.text.isNullOrBlank()) {
            isBlank = true
            etLow.error = "You must set a Message!"
        }
        return isBlank
    }

    private fun addConditionView(group: LinearLayout) {
        val conditionView = LayoutInflater.from(this.context).inflate(R.layout.layout_condition, group, false)
        val buttonDelete = conditionView.findViewById<ImageView>(R.id.condition_delete)
        buttonDelete.setOnClickListener {
            group.removeView(conditionView)
            conditionViewList.remove(conditionView)
        }
        conditionViewList.add(conditionView)
        group.addView(conditionView)
    }

    private fun extractCondition(): List<CachedResponse> {
        val responses = mutableListOf<CachedResponse>()
        for (view in conditionViewList) {
            val etLow = conditionViewList[conditionViewList.lastIndex].findViewById<EditText>(R.id.low_value)
            val etHigh = conditionViewList[conditionViewList.lastIndex].findViewById<EditText>(R.id.high_value)
            val etMessage = conditionViewList[conditionViewList.lastIndex].findViewById<EditText>(R.id.decision_message)

            if (!isConditionBlank(view)) responses.add(CachedResponse(etLow.text.toString().toFloat(), etHigh.text.toString().toFloat(), etMessage.text.toString()))
        }
        return responses
    }
}