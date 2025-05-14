package com.example.mongcare.presenter.devicestatus

class DeviceStatusPresenter(
    private val view: DeviceStatusContract.View
) : DeviceStatusContract.Presenter {

    override fun onConfirmClicked() {
        view.showToast("확인했습니다!")
    }

    override fun onBackPressed() {
        view.goBack()
    }
}
