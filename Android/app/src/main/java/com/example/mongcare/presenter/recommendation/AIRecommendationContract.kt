package com.example.mongcare.presenter.recommendation

interface AIRecommendationContract {
    interface View {
        fun addUserMessage(message: String)
        fun addAIMessage(message: String)
        fun clearInput()
    }

    interface Presenter {
        fun onSendClicked(input: String)
    }
}
