package com.example.quizzy.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.*
import com.example.quizzy.domain.QuizItem
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {
    private val TAG = "QUIZ-LIST"

    private val viewModel: HomeViewModel by viewModels (
            ownerProducer = {this}, factoryProducer = {ViewModelFactory(requireActivity().application)}
    )

    private var isReloadable = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, container, false)

        val adapter = QuizListAdapter(viewLifecycleOwner, object : OnQuizItemClickListener {
            override fun onItemClicked(quizItem: QuizItem) {
                viewModel.selectQuiz(quizItem)
                quizDialog()
//                Log.i(TAG, "onItemClicked: ${quizItem.creatorName} ${quizItem.id}")
//                viewModel.fetchQuizById(quizItem.id)
//                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQuizGameFragment(quizItem.id))
            }
        })
        val quizListView = rootView.findViewById<RecyclerView>(R.id.quiz_list)
        val loadingBar = rootView.findViewById<ProgressBar>(R.id.content_loading_bar)
        loadingBar.visibility = View.VISIBLE
        quizListView.adapter = adapter

        setOnScrollListener(quizListView)

        viewModel.endOfQuizList.observe(viewLifecycleOwner, {
            isReloadable = when(it) {
                true -> {
                    viewModel.fetchCount = 0
                    false
                }
                else -> true
            }
        })

        viewModel.liveQuizItemList.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) loadingBar.visibility = View.GONE
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.fetchedQuizId.observe(viewLifecycleOwner, {id ->
            if (!id.isNullOrBlank() && viewModel.navigateToQuizGame) {
                viewModel.navigateToQuizGame = false
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToQuizGameFragment(id))
            }
        })

        if (isReloadable) viewModel.fetchQuizList()
        return rootView
    }

    private fun setOnScrollListener(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (visibleItemCount + lastVisibleItem + 5 >= totalItemCount) {
                    if (isReloadable) viewModel.fetchQuizList()
                }
            }
        })
    }

    fun quizDialog() {
        val transaction = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag("quiz-dialog")
        prev?.let { transaction.remove(prev) }
        transaction.addToBackStack(null)
        transaction.commit()

        QuizPasswordAlertDialog().show(childFragmentManager, "quiz-dialog")
    }

    override fun onStart() {
        super.onStart()

        val parentActivity = requireActivity() as QuizGameActivity
        parentActivity.hideTopTextView()
        parentActivity.hideButton(R.id.button_back, R.id.button_complete, R.id.button_next)
    }
}

class QuizPasswordAlertDialog: DialogFragment() {
    private val viewModel: HomeViewModel by viewModels (
            ownerProducer = {requireParentFragment()}, factoryProducer = {ViewModelFactory(requireActivity().application)}
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_quiz_password, container, false)
        val passwordInput = rootView.findViewById<EditText>(R.id.quiz_password_input)
        val buttonEnter = rootView.findViewById<MaterialButton>(R.id.button_password_enter)
        var quizId = String()

        viewModel.selectedQuizItem.observe(viewLifecycleOwner, {quizItem ->
            quizId = quizItem.id
        })

        buttonEnter.setOnClickListener {
            viewModel.navigateToQuizGame = true
            val password = passwordInput.text.toString().trim()
            viewModel.fetchSelectedQuiz(quizId, password)
            dismiss()
        }

        return rootView
    }
}