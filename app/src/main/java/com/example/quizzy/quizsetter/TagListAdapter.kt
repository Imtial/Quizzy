package com.example.quizzy.quizsetter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R

class TagListAdapter(val context: Context) : ListAdapter<String, TagListAdapter.ViewHolder> (StringDiffCallback()){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            val tagView = itemView.findViewById<TextView>(R.id.tag)
            tagView.text = item
            itemView.invalidate()
        }
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val rootView = LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
                return ViewHolder(rootView)
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

class StringDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}