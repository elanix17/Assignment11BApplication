package com.example.assignment11bapplication.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment11bapplication.databinding.ItemLayoutBinding
import com.example.assignment11bapplication.model.Article

class ArticleListAdapter(private val clickListener: OnItemClickListener) :ListAdapter<Article, ArticleListAdapter.PropertyViewHolder>(DiffCallback) {

    class PropertyViewHolder(private var binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article, clickListener: OnItemClickListener) {
            binding.property = article
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                clickListener.onClick(article)
            }
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title === newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val  article = getItem(position)
        holder.bind(article, clickListener)
        setAnimation(holder.itemView)
    }
}
interface OnItemClickListener{
    fun onClick(article: Article)
}
private fun setAnimation(view: View){
    val anim= ScaleAnimation(
        0.0f,
        1.0f,
        0.0f,
        1.0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    anim.duration=800
    view.startAnimation(anim)
}