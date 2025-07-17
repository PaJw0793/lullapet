package com.example.mongcare.module

import android.os.Handler
import android.os.Looper
import android.util.Log
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
            Log.d("MistralAiApi", "API 키가 설정되지 않았습니다.")
            onError("API 키가 설정되지 않았습니다.")
            return
        }

        val json = JSONObject().apply {
            put("model", "mistral-tiny")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", question + "넌 지금부터 동물을 돌봐주는 수의사의 관점으로 사용자에게 알기쉽고 친숙하고 존댓말을 사용해서 답변해줘. " +
                            "그리고 답변은 10000자 이하로 해줘. " +
                            "그리고 답변은 반드시 한국어로 해줘.")
                })
            })
            // temperature, max_tokens 등 옵션 필요시 여기에 추가 가능
        }

        Log.d("MistralAiApi", "Request JSON: $json")

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url(apiUrl)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()

        Log.d("MistralAiApi", "Request Headers: ${request.headers}")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MistralAiApi", "onFailure: ${e.message}", e)
                Handler(Looper.getMainLooper()).post {
                    onError(e.message ?: "Unknown error")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("MistralAiApi", "HTTP Response Code: ${response.code}")
                val responseBody = response.body?.string()
                Log.d("MistralAiApi", "Response Body: $responseBody")
                if (!response.isSuccessful) {
                    Log.e("MistralAiApi", "Unsuccessful response: ${response.code}")
                    Handler(Looper.getMainLooper()).post {
                        onError("HTTP ${response.code}")
                    }
                    return
                }
                if (responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val choices = jsonResponse.optJSONArray("choices")
                        if (choices != null && choices.length() > 0) {
                            val messageObj = choices.getJSONObject(0).optJSONObject("message")
                            val message = messageObj?.optString("content") ?: ""
                            if (message.length > 10000) {
                                Log.e("MistralAiApi", "Response too large")
                                Handler(Looper.getMainLooper()).post {
                                    onError("Response too large")
                                }
                                return
                            }
                            Log.d("MistralAiApi", "AI Response Message: $message")
                            Handler(Looper.getMainLooper()).post {
                                onSuccess(message)
                            }
                        } else {
                            Log.e("MistralAiApi", "No answer found in response")
                            Handler(Looper.getMainLooper()).post {
                                onError("No answer found")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MistralAiApi", "Parse error: ${e.message}", e)
                        Handler(Looper.getMainLooper()).post {
                            onError("Parse error: ${e.message}")
                        }
                    }
                } else {
                    Log.e("MistralAiApi", "Empty response body")
                    Handler(Looper.getMainLooper()).post {
                        onError("Empty response")
                    }
                }
            }
        })
    }
}

