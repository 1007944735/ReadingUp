package com.sgevf.readingup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sgevf.readingup.R
import com.sgevf.readingup.model.ClickStepModel
import com.sgevf.readingup.model.ReturnBackStepModel
import com.sgevf.readingup.model.SleepStepModel
import com.sgevf.readingup.model.TaskStepModel

class TaskStepCreateViewModel(application: Application) : AndroidViewModel(application) {

    val action: MutableLiveData<TaskStepModel.StepAction> = MutableLiveData()

    val sleepDuration: MutableLiveData<String> = MutableLiveData()

    fun actionToString(value: TaskStepModel.StepAction?): String {
        return when (value) {
            TaskStepModel.StepAction.NONE -> ""
            TaskStepModel.StepAction.Click -> getApplication<Application>().getString(R.string.text_action_click)
            TaskStepModel.StepAction.ReturnBack -> getApplication<Application>().getString(R.string.text_action_return_back)
            TaskStepModel.StepAction.Sleep -> getApplication<Application>().getString(R.string.text_action_sleep)
            else -> ""
        }
    }

    fun buildTaskStep(): TaskStepModel? {
        return when (action.value) {
            TaskStepModel.StepAction.NONE -> null
            TaskStepModel.StepAction.Click -> ClickStepModel(1, 1)
            TaskStepModel.StepAction.ReturnBack -> ReturnBackStepModel()
            TaskStepModel.StepAction.Sleep -> SleepStepModel(sleepDuration.value?.toLong() ?: 0L)
            null -> null
        }
    }
}