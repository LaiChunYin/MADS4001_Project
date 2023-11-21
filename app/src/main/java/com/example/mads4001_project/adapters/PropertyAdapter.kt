package com.example.mads4001_project.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mads4001_project.databinding.ItemPropertyBinding

class PropertyAdapter(private var properties: MutableList<Property>) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    fun updateProperties(newProperties: List<Property>) {
        properties.clear()
        properties.addAll(newProperties)
        notifyDataSetChanged()
    }

    class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            // Assuming you want to display the property type as the title
            binding.propertyTitleTextView.text = property.type
            binding.propertyDescriptionTextView.text = property.description
            // Here you can set the other attributes of your property to different views, for example:
            // binding.numOfRoomsTextView.text = property.numOfRooms.toString()

            // If you have an image URL, you would load the image here. For example:
            // Glide.with(binding.root.context).load(property.imageUrl).into(binding.propertyImageView)
        }
    }
}
