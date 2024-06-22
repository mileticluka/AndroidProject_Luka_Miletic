package com.example.recipeapp.dao

import android.content.Context

fun getDatabase(context: Context?) = MealsSqlHelper(context)
