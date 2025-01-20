package com.ken.quizapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ken.quizapp.databinding.ItemQuizCategoryBinding
import com.ken.quizapp.models.CategoryModel
import com.ken.quizapp.ui.QuizActivity

class QuizCategoryAdapter(val categoryModel: List<CategoryModel>, val ctx: Context) :
    RecyclerView.Adapter<QuizCategoryAdapter.QuizCategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizCategoryViewHolder {
        val binding = ItemQuizCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizCategoryViewHolder, position: Int) {
        holder.bind(categoryModel = categoryModel[position])
        holder.itemView.setOnClickListener {
            ctx.startActivity(
                Intent(ctx, QuizActivity::class.java).putExtra(
                    "TYPE",
                    categoryModel[position].title
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return categoryModel.size
    }

    class QuizCategoryViewHolder(val binding: ItemQuizCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryModel: CategoryModel) {
            binding.category = categoryModel
            Log.d("QuizCategoryData", "Data: $categoryModel")
        }
    }
}