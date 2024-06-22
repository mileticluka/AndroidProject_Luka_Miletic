package com.example.recipeapp.api

import com.example.recipeapp.models.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPIService {
    @GET("search.php")
    fun searchMeals(@Query("s") query: String): Call<MealResponse>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") mealId: String): Call<MealResponse>
}