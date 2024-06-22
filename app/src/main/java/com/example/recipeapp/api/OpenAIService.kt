package com.example.recipeapp.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApiService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun getChatCompletion(@Body requestBody: JsonObject): Call<JsonObject>
}

class OpenAiService(apiKey: String) {

    private val openAiApiService: OpenAiApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        openAiApiService = retrofit.create(OpenAiApiService::class.java)
    }

    fun getChatCompletion(prompt: String): Call<JsonObject> {
        val requestBody = JsonObject().apply {
            addProperty("model", "gpt-3.5-turbo")
            add("messages", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("role", "system")
                    addProperty("content", "You are an AI designed to help with cooking and recipes.")
                })
                add(JsonObject().apply {
                    addProperty("role", "user")
                    addProperty("content", prompt)
                })
            })
        }

        return openAiApiService.getChatCompletion(requestBody)
    }
}
