package com.sgevf.readingup.model

class ReturnBackStepModel : TaskStepModel(StepAction.ReturnBack) {
    override fun describe(): String {
        return "返回上一页"
    }
}
