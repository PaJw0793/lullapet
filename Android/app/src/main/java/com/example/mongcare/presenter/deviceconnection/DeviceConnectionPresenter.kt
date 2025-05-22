package com.example.mongcare.presenter.deviceconnection

import com.example.mongcare.R

class DeviceConnectionPresenter(
    private val view: DeviceConnectionContract.View
) : DeviceConnectionContract.Presenter {

    override fun onCreate() {
        val items = listOf(
            Triple(R.drawable.dog_face, "강아지1의\n웨어러블 상태 보기/\n기기 연결 확인") {
                view.showToast("강아지1 클릭")
            },
//            Triple(R.drawable.ic_dog_face2, "강아지2의\n웨어러블 상태 보기/\n기기 연결 확인") {
//                view.showToast("강아지2 클릭")
//            },
//            Triple(R.drawable.ic_cat_face, "고양이의\n웨어러블 상태 보기/\n기기 연결 확인") {
//                view.showToast("고양이 클릭")
//            }
        )

        items.forEach { (icon, label, onClick) ->
            view.addDeviceItem(icon, label, onClick)
        }
    }

    override fun onBackPressed() {
        view.goBack()
    }
}
