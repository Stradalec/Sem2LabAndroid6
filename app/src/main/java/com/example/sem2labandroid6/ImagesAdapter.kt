package com.example.sem2labandroid6

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImagesAdapter(private val onLongClick: (Int) -> Unit) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private val items = mutableListOf<ImageItem>()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val descriptionView: TextView = view.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView)
            .load(item.uri)
            .into(holder.imageView)

        holder.descriptionView.text = item.description

        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount() = items.size
    fun updateItems(newItems: List<ImageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
