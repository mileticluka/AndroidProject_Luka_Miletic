package com.example.recipeapp.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.MealAdapter
import com.example.recipeapp.api.MealAPIService
import com.example.recipeapp.api.OpenAiService
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.framework.getTextPreference
import com.example.recipeapp.models.Meal
import com.example.recipeapp.models.MealResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView
    private lateinit var suggestionRecyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter
    private lateinit var favouriteButton: ImageButton
    private lateinit var timerButton: ImageButton
    private val mealList = mutableListOf<Meal>()

    private var randomFact = getString(R.string.loading)

    private val mealApiService: MealAPIService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPIService::class.java)
    }

    private lateinit var roundedCornersDrawable: Drawable
    private lateinit var roundedTopCornersDrawable: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        binding.FunFactTextView2.text = randomFact

        initViews()
        initListeners()

        getRandomFact()
    }

    private fun getRandomFact() {
        fun handleResponse(response: Response<JsonObject>) {
            if (response.isSuccessful) {
                val responseBody = response.body()
                val content = responseBody?.getAsJsonArray("choices")?.get(0)?.asJsonObject
                    ?.getAsJsonObject("message")?.get("content")?.asString

                randomFact = content.toString()
            } else {
                randomFact = "Error occurred with OpenAI API."
                println("Error occurred: ${response.raw()}")
            }
        }

        val openAiService = OpenAiService(getTextPreference("open-api-key")!!)
        val prompt = "Give me a random food fact in 50 words or less."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = openAiService.getChatCompletion(prompt).execute()

                withContext(Dispatchers.Main) {
                    handleResponse(response)
                }
            } catch (e: Exception) {
                println("Error occurred while calling OpenAI API. ${e.message}")
            }

            binding.FunFactTextView2.text = randomFact
        }
    }

    private fun initViews() {
        searchView = binding.searchView
        suggestionRecyclerView = binding.suggestionRecyclerView

        mealAdapter = MealAdapter(this, mealList) { selectedMeal ->
            handleSearch(selectedMeal)
        }

        with(suggestionRecyclerView) {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            visibility = View.GONE
        }

        searchView.background = ContextCompat.getDrawable(this, R.drawable.rounded_corners)!!
        roundedCornersDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_corners)!!
        roundedTopCornersDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corners)!!
    }

    private fun initListeners() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if ((newText?.length ?: 0) > 2) {
                    showSuggestions(newText)
                } else {
                    hideSuggestions()
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            hideSuggestions()
            false
        }

        favouriteButton = findViewById(R.id.button_favorites)
        favouriteButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FavouritesActivity::class.java)
            startActivity(intent)
        }

        timerButton = findViewById(R.id.button_timer)
        timerButton.setOnClickListener {
            val intent = Intent(this@MainActivity, TimerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleSearch(query: String?) {
            val intent = Intent(this, MealDetailActivity::class.java)
            intent.putExtra("MEAL_ID", query)
            startActivity(intent)
    }

    private fun showSuggestions(query: String?) {
        if (query.isNullOrBlank() || query.length <= 2) {
            hideSuggestions()
            return
        }

        mealApiService.searchMeals(query).enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        mealList.apply {
                            clear()
                            addAll(meals)
                        }
                        mealAdapter.notifyDataSetChanged()
                        suggestionRecyclerView.layoutParams.height =
                            calculateRecyclerViewHeight(mealList.size)
                        suggestionRecyclerView.visibility = View.VISIBLE
                        searchView.background = roundedTopCornersDrawable
                    } else {
                        hideSuggestions()
                    }
                } else {
                    hideSuggestions()
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                hideSuggestions()
            }
        })
    }

    private fun hideSuggestions() {
        suggestionRecyclerView.visibility = View.GONE
        searchView.background = roundedCornersDrawable
    }

    private fun calculateRecyclerViewHeight(itemCount: Int): Int {
        val itemHeight = resources.getDimension(R.dimen.suggestion_item_height).toInt()
        return min(itemCount * itemHeight, resources.getDimension(R.dimen.max_recycler_view_height).toInt())
    }
}