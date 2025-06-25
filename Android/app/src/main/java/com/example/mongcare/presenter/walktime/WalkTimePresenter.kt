package com.example.mongcare.presenter.walktime

import java.util.Locale

class WalkTimePresenter(private val view: WalkTimeContract.View) : WalkTimeContract.Presenter {

    override fun onAddButtonClicked(hour: Int, minute: Int) {
        // Presenter의 역할: 시간을 형식화하는 비즈니스 로직 처리
        val formattedTime = if (hour < 12) {
            String.format(Locale.getDefault(), "오전 %02d:%02d", hour, minute)
        } else {
            // 24시 형식에서 12시를 넘어가는 경우 오후 시간으로 변환
            String.format(Locale.getDefault(), "오후 %02d:%02d", if (hour == 12) 12 else hour - 12, minute)
        }

        // Presenter의 역할: 형식화된 시간을 View에게 전달하여 UI 업데이트를 요청
        view.addTimeEntryToView(formattedTime)
    }
}