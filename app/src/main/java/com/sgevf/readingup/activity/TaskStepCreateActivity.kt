package com.sgevf.readingup.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.sgevf.readingup.BaseActivity
import com.sgevf.readingup.R
import com.sgevf.readingup.databinding.ActivityTaskStepCreateBinding
import com.sgevf.readingup.fragment.ClickStepFragment
import com.sgevf.readingup.fragment.ReturnBackStepFragment
import com.sgevf.readingup.fragment.SleepStepFragment
import com.sgevf.readingup.model.TaskStepModel
import com.sgevf.readingup.viewmodel.TaskStepCreateViewModel
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet
import kotlin.math.log

class TaskStepCreateActivity : BaseActivity() {

    private lateinit var mDataBinding: ActivityTaskStepCreateBinding
    private lateinit var mViewModel: TaskStepCreateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this)[TaskStepCreateViewModel::class.java]
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_step_create)
        mDataBinding.lifecycleOwner = this
        mDataBinding.taskStepVM = mViewModel

        mDataBinding.titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(titleBar: TitleBar?) {
                finish()
            }
        })

        mDataBinding.tvChooseTask.setOnClickListener {
            showStepActionBottomDialog()
        }

        mDataBinding.btnComplete.setOnClickListener {
            val step = mViewModel.buildTaskStep()
            if (step == null) {

            } else {
                Log.d("test", "onCreate: " + step.describe())
            }
        }

        mViewModel.action.observe(this) {
            when (it) {
                TaskStepModel.StepAction.NONE -> return@observe
                TaskStepModel.StepAction.Click -> supportFragmentManager.beginTransaction()
                    .replace(mDataBinding.fragmentContainer.id, ClickStepFragment()).commit()

                TaskStepModel.StepAction.ReturnBack -> supportFragmentManager.beginTransaction()
                    .replace(mDataBinding.fragmentContainer.id, ReturnBackStepFragment()).commit()

                TaskStepModel.StepAction.Sleep -> supportFragmentManager.beginTransaction()
                    .replace(mDataBinding.fragmentContainer.id, SleepStepFragment()).commit()
            }

        }


    }

    private fun showStepActionBottomDialog() {
        val actionSet = TaskStepModel.StepAction.values().filter {
            it != TaskStepModel.StepAction.NONE
        }
        val actionStringSet = actionSet.map {
            mViewModel.actionToString(it)
        }
        BottomSheet.BottomListSheetBuilder(this)
            .apply {
                actionStringSet.forEach {
                    addItem(it)
                }
            }
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                if (mViewModel.action.value != actionSet[position]) {
                    mViewModel.action.value = actionSet[position]
                }
                dialog.dismiss()
            }
            .build()
            .show()
    }
}