
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.models.Meal

class MealAdapter(
    private val context: Context,
    private val mealList: List<Meal>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealList[position]

        holder.bind(meal)

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.BLACK)
        } else {
            holder.itemView.setBackgroundColor(Color.DKGRAY)
        }

        holder.itemView.setOnClickListener {
            onItemClick(meal.idMeal)
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealTextView: TextView = itemView.findViewById(R.id.mealTextView)

        fun bind(meal: Meal) {
            mealTextView.text = meal.strMeal
        }
    }
}
