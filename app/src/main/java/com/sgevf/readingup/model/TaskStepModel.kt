package com.sgevf.readingup.model

data class TaskStepModel(val action: StepAction) {

    enum class StepAction {
        /**
         * 单击
         */
        Click,

        /**
         * 返回上一页
         */
        ReturnBack,

        /**
         * 间隔
         */
        Sleep
    }
}
