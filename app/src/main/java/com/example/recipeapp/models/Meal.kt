package com.example.recipeapp.models

import com.google.gson.annotations.SerializedName

data class Meal (

    @SerializedName("idMeal"                      ) var idMeal                      : String,
    @SerializedName("strMeal"                     ) var strMeal                     : String? = null,
    @SerializedName("strDrinkAlternate"           ) var strDrinkAlternate           : String? = null,
    @SerializedName("strCategory"                 ) var strCategory                 : String? = null,
    @SerializedName("strArea"                     ) var strArea                     : String? = null,
    @SerializedName("strInstructions"             ) var strInstructions             : String? = null,
    @SerializedName("strMealThumb"                ) var strMealThumb                : String? = null,
    @SerializedName("strTags"                     ) var strTags                     : String? = null,
    @SerializedName("strYoutube"                  ) var strYoutube                  : String? = null,
    @SerializedName("strIngredient1"              ) var strIngredient1              : String? = null,
    @SerializedName("strIngredient2"              ) var strIngredient2              : String? = null,
    @SerializedName("strIngredient3"              ) var strIngredient3              : String? = null,
    @SerializedName("strIngredient4"              ) var strIngredient4              : String? = null,
    @SerializedName("strIngredient5"              ) var strIngredient5              : String? = null,
    @SerializedName("strIngredient6"              ) var strIngredient6              : String? = null,
    @SerializedName("strIngredient7"              ) var strIngredient7              : String? = null,
    @SerializedName("strIngredient8"              ) var strIngredient8              : String? = null,
    @SerializedName("strIngredient9"              ) var strIngredient9              : String? = null,
    @SerializedName("strIngredient10"             ) var strIngredient10             : String? = null,
    @SerializedName("strIngredient11"             ) var strIngredient11             : String? = null,
    @SerializedName("strIngredient12"             ) var strIngredient12             : String? = null,
    @SerializedName("strIngredient13"             ) var strIngredient13             : String? = null,
    @SerializedName("strIngredient14"             ) var strIngredient14             : String? = null,
    @SerializedName("strIngredient15"             ) var strIngredient15             : String? = null,
    @SerializedName("strIngredient16"             ) var strIngredient16             : String? = null,
    @SerializedName("strIngredient17"             ) var strIngredient17             : String? = null,
    @SerializedName("strIngredient18"             ) var strIngredient18             : String? = null,
    @SerializedName("strIngredient19"             ) var strIngredient19             : String? = null,
    @SerializedName("strIngredient20"             ) var strIngredient20             : String? = null,
    @SerializedName("strMeasure1"                 ) var strMeasure1                 : String? = null,
    @SerializedName("strMeasure2"                 ) var strMeasure2                 : String? = null,
    @SerializedName("strMeasure3"                 ) var strMeasure3                 : String? = null,
    @SerializedName("strMeasure4"                 ) var strMeasure4                 : String? = null,
    @SerializedName("strMeasure5"                 ) var strMeasure5                 : String? = null,
    @SerializedName("strMeasure6"                 ) var strMeasure6                 : String? = null,
    @SerializedName("strMeasure7"                 ) var strMeasure7                 : String? = null,
    @SerializedName("strMeasure8"                 ) var strMeasure8                 : String? = null,
    @SerializedName("strMeasure9"                 ) var strMeasure9                 : String? = null,
    @SerializedName("strMeasure10"                ) var strMeasure10                : String? = null,
    @SerializedName("strMeasure11"                ) var strMeasure11                : String? = null,
    @SerializedName("strMeasure12"                ) var strMeasure12                : String? = null,
    @SerializedName("strMeasure13"                ) var strMeasure13                : String? = null,
    @SerializedName("strMeasure14"                ) var strMeasure14                : String? = null,
    @SerializedName("strMeasure15"                ) var strMeasure15                : String? = null,
    @SerializedName("strMeasure16"                ) var strMeasure16                : String? = null,
    @SerializedName("strMeasure17"                ) var strMeasure17                : String? = null,
    @SerializedName("strMeasure18"                ) var strMeasure18                : String? = null,
    @SerializedName("strMeasure19"                ) var strMeasure19                : String? = null,
    @SerializedName("strMeasure20"                ) var strMeasure20                : String? = null,
    @SerializedName("strSource"                   ) var strSource                   : String? = null,
    @SerializedName("strImageSource"              ) var strImageSource              : String? = null,
    @SerializedName("strCreativeCommonsConfirmed" ) var strCreativeCommonsConfirmed : String? = null,
    @SerializedName("dateModified"                ) var dateModified                : String? = null
) {
    fun getIngredientsArray(): List<String> {
        val ingredients = mutableListOf<String>()

        for (i in 1..20) {
            val ingredient = getIngredient(i)
            if (!ingredient.isNullOrBlank()) {
                val measure = getMeasure(i)
                val formattedIngredient = "$measure $ingredient"
                ingredients.add(formattedIngredient)
            }
        }

        return ingredients
    }

    private fun getIngredient(index: Int): String? {
        return when (index) {
            1 -> strIngredient1
            2 -> strIngredient2
            3 -> strIngredient3
            4 -> strIngredient4
            5 -> strIngredient5
            6 -> strIngredient6
            7 -> strIngredient7
            8 -> strIngredient8
            9 -> strIngredient9
            10 -> strIngredient10
            11 -> strIngredient11
            12 -> strIngredient12
            13 -> strIngredient13
            14 -> strIngredient14
            15 -> strIngredient15
            16 -> strIngredient16
            17 -> strIngredient17
            18 -> strIngredient18
            19 -> strIngredient19
            20 -> strIngredient20
            else -> null
        }
    }

    private fun getMeasure(index: Int): String? {
        return when (index) {
            1 -> strMeasure1
            2 -> strMeasure2
            3 -> strMeasure3
            4 -> strMeasure4
            5 -> strMeasure5
            6 -> strMeasure6
            7 -> strMeasure7
            8 -> strMeasure8
            9 -> strMeasure9
            10 -> strMeasure10
            11 -> strMeasure11
            12 -> strMeasure12
            13 -> strMeasure13
            14 -> strMeasure14
            15 -> strMeasure15
            16 -> strMeasure16
            17 -> strMeasure17
            18 -> strMeasure18
            19 -> strMeasure19
            20 -> strMeasure20
            else -> null
        }
    }
}
