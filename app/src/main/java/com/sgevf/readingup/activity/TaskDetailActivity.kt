package com.sgevf.readingup.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sgevf.readingup.R
import com.sgevf.readingup.databinding.ActivityTaskDetailBinding

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var mDataBinding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail)
    }
}