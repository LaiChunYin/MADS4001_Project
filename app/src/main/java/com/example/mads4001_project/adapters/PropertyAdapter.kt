package com.example.mads4001_project.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mads4001_project.databinding.ItemPropertyBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.screens.LoginActivity
import com.example.mads4001_project.screens.PropertyDetailActivity
import com.google.gson.Gson
import android.view.View
import com.example.mads4001_project.R
import com.example.mads4001_project.utils.getLoggedInUser
import com.example.mads4001_project.utils.saveDataToSharedPref
import com.example.mads4001_project.utils.sharedPreferences
import com.google.android.material.snackbar.Snackbar

class PropertyAdapter(private var properties: MutableList<Property>, private var loggedInUserName: String, private val showShortlistOnly: Boolean) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
    private var loggedInUser: User? = null
    private val shortlistedProperties: MutableList<String> = mutableListOf()
    private val tag = "Property Adapter"

    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property, context: Context, pos: Int) {
            if(loggedInUserName != ""){
                loggedInUser = getLoggedInUser(loggedInUserName, context)
                shortlistedProperties.clear()
                for (property in loggedInUser!!.shortlistedProperties){
                    shortlistedProperties.add(property.address)
                }
            }

            // Assuming you want to display the property type as the title
            binding.propertyTitleTextView.text = property.type
            binding.propertyDescriptionTextView.text = property.description
            binding.propertyAddressTextView.text = property.address
            binding.propertyCityPostalTextView.text = "${property.city}, ${property.postalCode}"
//            Glide.with(binding.root.context).load(property.imageUrl).into(binding.propertyImage)  // for online images

            val imageName = property.imageUrl ?: "default_image"
            val res = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            this.binding.propertyImage.setImageResource(res)

            if(loggedInUser != null && loggedInUser?.userType == "Tenant" && shortlistedProperties.contains(property.address)){
                binding.removeBtn.visibility = View.VISIBLE
                binding.shortListBtn.visibility = View.GONE
            }
            else if(loggedInUser != null && loggedInUser?.userType == "Tenant"){
                binding.shortListBtn.visibility = View.VISIBLE
                binding.removeBtn.visibility = View.GONE
            }
            else {
                binding.shortListBtn.visibility = View.GONE
                binding.removeBtn.visibility = View.GONE
            }


            binding.propertyCard.setOnClickListener {
                // popup property details
                if(this@PropertyAdapter.loggedInUser != null){
                    Log.i(tag, "${loggedInUser} logged in")
                    val intent = Intent(context, PropertyDetailActivity::class.java)
                    intent.putExtra("PROPERTY", property)
                    context.startActivity(intent)
                }
                else {
                    Log.i(tag, "no one logged in")
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.putExtra("REFERER", "MainActivity")
                    context.startActivity(intent)
                }
            }

            binding.shortListBtn.setOnClickListener {
                loggedInUser = Gson().fromJson(sharedPreferences.getString(loggedInUser?.username, ""), User::class.java)
                loggedInUser!!.shortlistedProperties.add(property)
                saveDataToSharedPref(context, "USERS", loggedInUser!!.username, loggedInUser!!, true)

                if(showShortlistOnly) {
                    properties = loggedInUser!!.shortlistedProperties
                }
                this@PropertyAdapter.notifyDataSetChanged()
            }

            binding.removeBtn.setOnClickListener {
                loggedInUser = getLoggedInUser(loggedInUserName, context)

                for(i in 0..< loggedInUser!!.shortlistedProperties.size){
                    if(loggedInUser!!.shortlistedProperties[i].equals(property)){
                        loggedInUser!!.shortlistedProperties.removeAt(i)
                        break
                    }
                }
                saveDataToSharedPref(context, "USERS", loggedInUser!!.username, loggedInUser!!, true)

                if(showShortlistOnly){
                    properties = loggedInUser!!.shortlistedProperties
                }
                this@PropertyAdapter.notifyDataSetChanged()
            }

            // Set the availability tag based on the availableForRent property
            val availabilityTag = binding.propertyAvailabilityTag
            if (property.availableForRent) {
                // Property is available
                availabilityTag.text = context.getString(R.string.available)
                availabilityTag.visibility = View.VISIBLE
                availabilityTag.setBackgroundResource(R.drawable.available_tag_background) // Green background for available
            } else {
                // Property is not available
                availabilityTag.text = context.getString(R.string.not_available)
                availabilityTag.visibility = View.VISIBLE
                availabilityTag.setBackgroundResource(R.drawable.not_available_tag_background) // Red background for not available
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        val context = holder.itemView.context
        holder.bind(property, context, position)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

}
