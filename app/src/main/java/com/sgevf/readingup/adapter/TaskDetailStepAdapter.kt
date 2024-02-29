package com.sgevf.readingup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.sgevf.readingup.databinding.LayoutTaskStepBinding
import com.sgevf.readingup.model.TaskStepModel

class TaskDetailStepAdapter :
    BaseQuickAdapter<TaskStepModel, TaskDetailStepAdapter.VH>() {

    class VH(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutTaskStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    )

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: TaskStepModel?
    ) {
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(parent)
    }
}