package com.example.mongcare.presenter

import com.example.mongcare.model.DataModel
import com.example.mongcare.view.MainView

class MainPresenter(private val view: MainView) {
    private val model = DataModel()

    fun loadData() {
        val data = model.fetchData()
        view.showData(data)
    }
}
