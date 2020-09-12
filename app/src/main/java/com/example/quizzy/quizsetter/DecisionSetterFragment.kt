package com.example.quizzy.quizsetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizzy.OnButtonClickListener
import com.example.quizzy.QuizGameActivity
import com.example.quizzy.R
import com.google.android.material.button.MaterialButton

class DecisionSetterFragment: Fragment() {

    private val conditionViewList = mutableListOf<View>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(context).inflate(R.layout.fragment_decision_setter, container, false)

        val buttonAddDecision = rootView.findViewById<MaterialButton>(R.id.button_add_condition)
        val conditionContainerView = rootView.findViewById<LinearLayout>(R.id.condition_container)

        (requireActivity() as QuizGameActivity).setOnButtonClickListener(object : OnButtonClickListener {
            override fun nextButtonClicked() {
                TODO("Not yet implemented")
            }

            override fun completeButtonClicked() {
                findNavController().navigate(DecisionSetterFragmentDirections.actionDecisionSetterFragmentToPublishQuizFragment())
            }

            override fun backButtonClicked() {
                TODO("Not yet implemented")
            }
        })

        buttonAddDecision.setOnClickListener {
            addConditionView(conditionContainerView)
        }

        return rootView
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
}