package com.sgevf.readingup.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.sgevf.readingup.BaseActivity
import com.sgevf.readingup.R
import com.sgevf.readingup.databinding.ActivityTaskStepCreateBinding

class TaskStepCreateActivity: BaseActivity() {

    private lateinit var mDataBinding: ActivityTaskStepCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_step_create)

        mDataBinding.titleBar.setOnTitleBarListener(object:OnTitleBarListener {
            override fun onLeftClick(titleBar: TitleBar?) {
                finish()
            }
        })

        mDataBinding.tvChooseTask.setOnClickListener {

        }

        mDataBinding.btnComplete.setOnClickListener {

        }



    }
}