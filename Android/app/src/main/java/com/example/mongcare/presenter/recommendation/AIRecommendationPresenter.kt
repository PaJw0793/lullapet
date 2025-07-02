package com.example.mongcare.presenter.recommendation

import com.example.mongcare.module.MistralAiApi

class AIRecommendationPresenter(
    private val view: AIRecommendationContract.View,
    apiKey: String
) : AIRecommendationContract.Presenter {

    private val mistralApi = MistralAiApi().apply { setApiKey(apiKey) }

    override fun onSendClicked(input: String) {
        if (input.isBlank()) return

        view.addUserMessage(input)

        mistralApi.askQuestion(
            question = input,
            onSuccess = { answer ->
                view.addAIMessage(answer)
            },
            onError = { error ->
                view.addAIMessage("AI 응답 오류: $error")
            }
        )

        view.clearInput()
    }
}
