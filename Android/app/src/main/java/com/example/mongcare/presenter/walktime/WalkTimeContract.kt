package com.example.mongcare.presenter.walktime

import android.view.View
import android.view.ViewGroup

interface WalkTimeContract {
    interface View {
        fun addTimeEntryToView(formattedTime: String)
        fun removeTimeEntryFromView(view: View)
        // Presenter가 View의 특정 요소를 직접 조작하지 않고, View가 컨테이너를 제공하도록 함.
        fun getWalkTimeListContainer(): ViewGroup
    }

    interface Presenter {
        fun onAddButtonClicked(hour: Int, minute: Int)
    }
}