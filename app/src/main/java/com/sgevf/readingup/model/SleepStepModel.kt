package com.sgevf.readingup.model

class SleepStepModel(var duration: Long) : TaskStepModel(StepAction.Sleep) {
    override fun describe(): String {
        return "间隔${duration}毫秒"

    }
}