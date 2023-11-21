package com.example.mads4001_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
            binding.propertyAddressTextView.text = property.address
            binding.propertyCityPostalTextView.text = "${property.city}, ${property.postalCode}"
            binding.propertyDescriptionTextView.text = property.description
            Glide.with(binding.root.context)
                .load(property.imageURL)
                .into(binding.propertyImageView)

            // Bind additional details as needed
            // If you have an image URL for the property, load it into the ImageView here
        }
    }
}
