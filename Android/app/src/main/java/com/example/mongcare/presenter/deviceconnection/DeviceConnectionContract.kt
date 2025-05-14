package com.example.mongcare.presenter.deviceconnection

interface DeviceConnectionContract {

    interface View {
        fun addDeviceItem(iconRes: Int, label: String, onClick: () -> Unit)
        fun goBack()
        fun showToast(msg: String)
    }

    interface Presenter {
        fun onCreate()
        fun onBackPressed()
    }
}
