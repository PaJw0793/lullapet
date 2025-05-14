package com.example.mongcare.presenter.devicestatus

interface DeviceStatusContract {

    interface View {
        fun showToast(msg: String)
        fun goBack()
    }

    interface Presenter {
        fun onConfirmClicked()
        fun onBackPressed()
    }
}
