package com.example.mongcare.presenter.walktime

import java.util.Locale

class WalkTimePresenter(private val view: WalkTimeContract.View) : WalkTimeContract.Presenter {

    override fun onAddButtonClicked(hour: Int, minute: Int) {
        val formattedTime = if (hour < 12) {
            String.format(Locale.getDefault(), "오전 %02d:%02d", hour, minute)
        } else {
           String.format(Locale.getDefault(), "오후 %02d:%02d", if (hour == 12) 12 else hour - 12, minute)
       }
        view.addTimeEntryToView(formattedTime)
   }
}