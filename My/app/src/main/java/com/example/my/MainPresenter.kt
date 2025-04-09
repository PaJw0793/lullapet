package com.example.my

class MainPresenter(private val view: MainView) {
    private val model = MainModel()

    fun onButtonClick(input: String) {
        val result = model.processData(input)
        view.showResult(result)
    }
}
