package com.example.mongcare.presenter.walktime

import android.view.ViewGroup

/**
 * View와 Presenter 간의 통신 규칙을 정의하는 '계약서' 입니다.
 * 이 인터페이스를 통해 View와 Presenter는 서로를 직접 참조하지 않고,
 * 오직 정의된 기능(메서드)을 통해서만 상호작용합니다. (느슨한 결합)
 */
interface WalkTimeContract {

    /**
     * View가 구현해야 할 기능 목록입니다.
   * Presenter는 이 인터페이스에 정의된 메서드만 호출하여 UI를 제어할 수 있습니다.*/
    interface View {
        // Presenter가 시간을 가공해서 주면, View는 이 메서드를 통해 화면에 시간 항목을 추가해야 합니다.
        fun addTimeEntryToView(formattedTime: String)
//
//        // 화면에서 특정 시간 항목 View를 제거해야 합니다.
        fun removeTimeEntryFromView(view: android.view.View)
//
//        // 시간 항목들이 들어갈 컨테이너 View를 반환해야 합니다.
        fun getWalkTimeListContainer(): ViewGroup
    }
//
//    /**
//     * Presenter가 구현해야 할 기능 목록입니다.
//     * View는 사용자의 액션(예: 버튼 클릭)이 발생했을 때,
//     * 이 인터페이스에 정의된 메서드를 호출하여 Presenter에게 알려줍니다.
//     */
    interface Presenter {
        // View로부터 '추가하기' 버튼이 눌렸음을 전달받고, 시간/분 정보를 받습니다.
        fun onAddButtonClicked(hour: Int, minute: Int)
    }
}