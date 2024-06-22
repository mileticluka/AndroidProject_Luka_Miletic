package com.example.recipeapp

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.api.MealAPIService
import com.example.recipeapp.api.OpenAiService
import com.example.recipeapp.dao.FAVOURITES_URI
import com.example.recipeapp.framework.getTextPreference
import com.example.recipeapp.models.FavouriteMeal
import com.example.recipeapp.models.Meal
import com.example.recipeapp.models.MealResponse
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealDetailActivity : AppCompatActivity() {

    private lateinit var mealAPIService: MealAPIService

    private lateinit var mealImageView: ImageView
    private lateinit var mealNameTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var ingredientsListView: ListView
    private lateinit var favouriteButton: Button

    private lateinit var editTextInput: EditText
    private lateinit var buttonSend: Button

    private lateinit var meal: Meal

    private lateinit var openAiService: OpenAiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail)

        mealAPIService = createMealAPIService()

        // Initialize views
        mealImageView = findViewById(R.id.mealImageView)
        mealNameTextView = findViewById(R.id.mealNameTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        ingredientsListView = findViewById(R.id.ingredientsListView)
        favouriteButton = findViewById(R.id.favouriteButton)
        buttonSend = findViewById(R.id.button_submit)
        editTextInput = findViewById(R.id.textChatInput)


        openAiService = OpenAiService(getTextPreference("open-api-key")!!)

        val mealId = intent.getStringExtra("MEAL_ID")

        registerEvents();
        if (mealId != null) {
            getMealDetails(mealId)
            favouriteButton.text = getString(R.string.loading)
        } else {
            // TODO: Handle the case where mealId is null (e.g., show an error message)
        }
    }

    private fun registerEvents() {
        favouriteButton.setOnClickListener {
            if (!isFavourite()) {
                contentResolver.insert(
                    FAVOURITES_URI,
                    ContentValues().apply {
                        put(FavouriteMeal::mealId.name, meal.idMeal)
                        put(FavouriteMeal::name.name, meal.strMeal)
                        put(FavouriteMeal::image.name, meal.strMealThumb)
                    }
                )
            } else {
                contentResolver.delete(
                    FAVOURITES_URI.buildUpon().appendPath(meal.idMeal).build(),
                    null, null
                )
            }

            refreshButton()
        }

        buttonSend.setOnClickListener {
            fun handleResponse(response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val content = responseBody?.getAsJsonArray("choices")?.get(0)?.asJsonObject
                        ?.getAsJsonObject("message")?.get("content")?.asString

                    editTextInput.setText(content)
                } else {
                    editTextInput.setText("An error has occurred contacting the AI")
                    println("Error occurred: ${response.raw()}")
                }
            }

            val prompt = "Recipe context: ${meal.strMeal} ${meal.strInstructions}. User prompt is: ${editTextInput.text}"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = openAiService.getChatCompletion(prompt).execute()

                    withContext(Dispatchers.Main) {
                        handleResponse(response)
                    }
                } catch (e: Exception) {
                    println("Error occurred while calling OpenAI API. ${e.message}")
                }
            }
        }
    }

    private fun createMealAPIService(): MealAPIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MealAPIService::class.java)
    }

    private fun getMealDetails(mealId: String) {
        val call = mealAPIService.getMealDetail(mealId)
        call.enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val mealResponse = response.body()
                    if (mealResponse != null && mealResponse.meals.isNotEmpty()) {
                        meal = mealResponse.meals[0]

                        // Update UI with meal details
                        Picasso.get().load(meal.strMealThumb).into(mealImageView)
                        mealNameTextView.text = meal.strMeal
                        instructionsTextView.text = meal.strInstructions

                        // Set ingredients to ListView
                        val ingredientsArrayAdapter = ArrayAdapter(
                            this@MealDetailActivity,
                            android.R.layout.simple_list_item_1,
                            meal.getIngredientsArray()
                        )

                        // Set the adapter to the ListView
                        ingredientsListView.adapter = ingredientsArrayAdapter

                        // Calculate the total height of all items in the ListView
                        var totalHeight = 0
                        for (i in 0 until ingredientsArrayAdapter.count) {
                            val listItem = ingredientsArrayAdapter.getView(i, null, ingredientsListView)
                            listItem.measure(0, 0)
                            totalHeight += listItem.measuredHeight
                        }

                        // Set the height of the ListView
                        val params = ingredientsListView.layoutParams
                        params.height = totalHeight + (ingredientsListView.dividerHeight * (ingredientsArrayAdapter.count - 1))
                        ingredientsListView.layoutParams = params
                        ingredientsListView.requestLayout()

                        refreshButton()
                    }
                } else {
                    System.out.println("failElse")
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                System.out.println("fail")
            }
        })
    }

    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    private fun isFavourite() : Boolean {
        val cursor = contentResolver.query(
            FAVOURITES_URI,
            arrayOf(FavouriteMeal::mealId.name),
            "${FavouriteMeal::mealId.name}=?",
            arrayOf(meal.idMeal),
            null)

        var exists = false;
        if (cursor?.moveToFirst() == true) {
            exists = true;
            cursor.close()
        }
        return exists
    }

    private fun refreshButton() {
        if (!isFavourite()) {
            favouriteButton.text = getString(R.string.add_favourite)
        } else {
            favouriteButton.text = getString(R.string.remove_favourite)
        }
    }
}

