package com.example.quizzy.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.*
import com.example.quizzy.domain.QuizItem

class HomeFragment : Fragment() {
    private val TAG = "QUIZ-LIST"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, container, false)

        val adapter = QuizListAdapter(viewLifecycleOwner, object : OnQuizItemClickListener {
            override fun onItemClicked(quizItem: QuizItem) {
                Log.i(TAG, "onItemClicked: ${quizItem.creatorName}")
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQuizGameFragment(quizItem.id))
            }
        })
        val quizListView = rootView.findViewById<RecyclerView>(R.id.quiz_list)
        val loadingBar = rootView.findViewById<ProgressBar>(R.id.content_loading_bar)
        loadingBar.visibility = View.VISIBLE
        quizListView.adapter = adapter


        val viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
                .get(HomeViewModel::class.java)

        viewModel.liveQuizItemList.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) loadingBar.visibility = View.GONE
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.fetchQuizList()
        return rootView
    }

    override fun onStart() {
        super.onStart()

        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideTopTextView()
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)
    }
}