package com.example.recipeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.FavouritesListAdapter
import com.example.recipeapp.dao.FAVOURITES_URI
import com.example.recipeapp.models.FavouriteMeal

class FavouritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavouritesListAdapter
    private lateinit var list: MutableList<FavouriteMeal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favourites)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        list = getAll()
        adapter = FavouritesListAdapter(list, this)

        recyclerView = findViewById(R.id.rvFavourites)
        recyclerView.apply {
            adapter = this@FavouritesActivity.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getAll() : MutableList<FavouriteMeal> {
        val cursor = contentResolver.query(
            FAVOURITES_URI,
            arrayOf(FavouriteMeal::mealId.name, FavouriteMeal::name.name, FavouriteMeal::image.name),
            null,
            null,
            null)

        val list = mutableListOf<FavouriteMeal>()
        if (cursor == null) return mutableListOf()

        while (cursor.moveToNext()) {
            list.add(FavouriteMeal(
                cursor.getLong(cursor.getColumnIndexOrThrow(FavouriteMeal::mealId.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(FavouriteMeal::name.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(FavouriteMeal::image.name)),
            ))
        }
        cursor.close()

        return list
    }
}