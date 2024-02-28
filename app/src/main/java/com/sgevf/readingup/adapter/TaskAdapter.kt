package com.sgevf.readingup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgevf.readingup.R

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, null)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class TaskViewHolder : RecyclerView.ViewHolder {

        constructor(itemView: View):super(itemView) {

        }

    }
}