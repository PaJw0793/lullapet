package com.example.mongcare.module

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MistralAiApi {
    private var apiKey: String? = null
    private val client = OkHttpClient()
    private val apiUrl = "https://api.mistral.ai/v1/chat/completions"

    fun setApiKey(key: String) {
        this.apiKey = key
    }

    fun askQuestion(
        question: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (apiKey.isNullOrBlank()) {
            onError("API 키가 설정되지 않았습니다.")
            return
        }

        val json = JSONObject()
        json.put("model", "mistral-tiny")
        val messages = JSONArray()
        messages.put(JSONObject().put("role", "user").put("content", question))
        json.put("messages", messages)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, json.toString())

        val builder = Request.Builder()
            .url(apiUrl)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")

        val request = builder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError("HTTP ${response.code}")
                    return
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val choices = jsonResponse.getJSONArray("choices")
                        if (choices.length() > 0) {
                            val message = choices.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                            onSuccess(message)
                        } else {
                            onError("No answer found")
                        }
                    } catch (e: Exception) {
                        onError("Parse error: ${e.message}")
                    }
                } else {
                    onError("Empty response")
                }
            }
        })
    }
}
