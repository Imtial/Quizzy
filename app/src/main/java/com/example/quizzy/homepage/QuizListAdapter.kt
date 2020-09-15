package com.example.quizzy.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.database.QuizItem
import com.example.quizzy.databinding.QuizListItemBinding
import com.example.quizzy.quizsetter.TagListAdapter

class QuizListAdapter(private val lifecycleOwner: LifecycleOwner, private val listener: OnQuizItemClickListener)
    : ListAdapter<QuizItem, QuizListAdapter.ViewHolder>(QuizItemDiffCallback()){

    class ViewHolder(private val binding: QuizListItemBinding, private val listener: OnQuizItemClickListener)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizItem) {
            binding.quizItem = item
            itemView.setOnClickListener { listener.onItemClicked(item) }
            binding.executePendingBindings()
        }

        companion object{
            fun from (parent: ViewGroup, lifecycleOwner: LifecycleOwner, listener: OnQuizItemClickListener)
                    : ViewHolder {
                val binding = QuizListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
                binding.lifecycleOwner = lifecycleOwner

                return ViewHolder(binding, listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, lifecycleOwner, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class QuizItemDiffCallback: DiffUtil.ItemCallback<QuizItem>() {
    override fun areItemsTheSame(oldItem: QuizItem, newItem: QuizItem): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: QuizItem, newItem: QuizItem): Boolean = oldItem == newItem
}

interface OnQuizItemClickListener {
    fun onItemClicked(quizItem: QuizItem)
}