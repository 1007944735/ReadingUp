package com.sgevf.readingup.model

abstract class TaskStepModel(var action: StepAction) {

    abstract fun describe(): String

    enum class StepAction {
        NONE,
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
