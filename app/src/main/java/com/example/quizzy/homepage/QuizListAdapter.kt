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

class QuizListAdapter : ListAdapter<QuizItem, QuizListAdapter.ViewHolder>(QuizItemDiffCallback()){

    class ViewHolder(private val binding: QuizListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizItem) {
            binding.quizItem = item
//            val adapter = TagListAdapter(binding.root.context)
//            binding.itemTagList.adapter = adapter
//            adapter.submitList(item.tags)
            binding.executePendingBindings()
        }

        companion object{
            fun from (parent: ViewGroup): ViewHolder {
                val binding = QuizListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
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