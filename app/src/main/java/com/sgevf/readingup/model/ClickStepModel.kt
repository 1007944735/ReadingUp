package com.sgevf.readingup.model

class ClickStepModel(var x: Int, var y: Int) :TaskStepModel(StepAction.Click) {
    override fun describe(): String {
        return "单击[$x,$y]"
    }
}