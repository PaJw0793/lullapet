package com.example.mongcare.presenter.walktime

import android.view.ViewGroup

interface WalkTimeContract {

    interface View {
        fun addTimeEntryToView(formattedTime: String)

        fun removeTimeEntryFromView(view: android.view.View)

        fun getWalkTimeListContainer(): ViewGroup
    }

    interface Presenter {
        fun onAddButtonClicked(hour: Int, minute: Int)
    }
}