package com.example.mongcare.util
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

object GeminiApiExample {
    private const val API_KEY = "AIzaSyAVlGMy5snFQfKACEiPysTfwuMgGq5cV08"
    private const val ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$API_KEY"

    fun sendPrompt(prompt: String, callback: (String?) -> Unit) {
        val client = OkHttpClient()
        val jsonBody = """
            {
              "contents": [{"parts":[{"text":"$prompt"}]}]
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(ENDPOINT)
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(null)
                        return
                    }
                    callback(response.body?.string())
                }
            }
        })
    }
}

// 사용 예시
fun main() {
    GeminiApiExample.sendPrompt("안녕, Gemini!") { response ->
        println("Gemini 응답: $response")
    }
}
