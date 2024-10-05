package com.example.quickbyte.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.quickbyte.Domain.Foods
import com.example.quickbyte.R
import com.example.quickbyte.StartActivity

class BestFoodsAdapter(
    private val context: Context,  // Context is passed via constructor
    private val items: ArrayList<Foods>  // List of food items is passed via constructor
) : RecyclerView.Adapter<BestFoodsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_best_deal, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = items[position]

        // Bind data to view components
        holder.titleTxt.text = foodItem.title
        holder.priceTxt.text = "$${foodItem.price}"
        holder.timeTxt.text = "${foodItem.timeValue} min"
        holder.starTxt.text = "${foodItem.star}"

        // Load image using Glide
        Glide.with(context)
            .load(foodItem.imagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.pic)

        // Set click listener to open DetailActivity
        // for now it will go to start activity change it later to detail activity remember
        holder.itemView.setOnClickListener {
            val intent = Intent(context, StartActivity::class.java)
            intent.putExtra("object", foodItem)  // Pass the food object
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size  // Return the size of the food items list
    }

    // ViewHolder class to hold view references
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleTxt)
        val priceTxt: TextView = itemView.findViewById(R.id.priceTxt)
        val starTxt: TextView = itemView.findViewById(R.id.starTxt)
        val timeTxt: TextView = itemView.findViewById(R.id.timeTxt)
        val pic: ImageView = itemView.findViewById(R.id.pic)
    }
}
