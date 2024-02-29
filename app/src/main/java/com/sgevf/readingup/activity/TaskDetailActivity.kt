package com.sgevf.readingup.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.sgevf.readingup.BaseActivity
import com.sgevf.readingup.R
import com.sgevf.readingup.adapter.TaskDetailStepAdapter
import com.sgevf.readingup.databinding.ActivityTaskDetailBinding

class TaskDetailActivity : BaseActivity() {
    private lateinit var mDataBinding: ActivityTaskDetailBinding
    private var mTaskDetailStepAdapter: TaskDetailStepAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail)
        mDataBinding.titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(titleBar: TitleBar?) {
                finish()
            }
        })

        mDataBinding.taskStepList.apply {
            layoutManager = LinearLayoutManager(this@TaskDetailActivity)
            mTaskDetailStepAdapter = TaskDetailStepAdapter()
            adapter = mTaskDetailStepAdapter
        }
    }
}