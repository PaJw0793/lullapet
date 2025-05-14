package com.example.mongcare.presenter.recommendation

class AIRecommendationPresenter(
    private val view: AIRecommendationContract.View
) : AIRecommendationContract.Presenter {

    override fun onSendClicked(input: String) {
        if (input.isBlank()) return

        view.addUserMessage(input)
        view.addAIMessage("~~~ 이런식으로 하면 강아지가 편안한 숙면을 취할 수 있을 거예요!")
        view.clearInput()
    }
}
