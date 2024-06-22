package com.example.recipeapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.activity.MealDetailActivity
import com.example.recipeapp.R
import com.example.recipeapp.models.FavouriteMeal
import com.squareup.picasso.Picasso

class FavouritesListAdapter(private val items: MutableList<FavouriteMeal>, private val context: Context)
    : RecyclerView.Adapter<FavouritesListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<ViewGroup>(R.id.container);
        private val title = itemView.findViewById<TextView>(R.id.textName);
        private val image = itemView.findViewById<ImageView>(R.id.mealImage)

        fun bind(meal: FavouriteMeal) {
            title.text = meal.name
            Picasso.get().load(meal.image).into(image)

            container.setOnClickListener {
                val intent = Intent(context, MealDetailActivity::class.java)
                intent.putExtra("MEAL_ID", meal.mealId.toString())
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return ViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {;
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}