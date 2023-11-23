package com.example.mads4001_project.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mads4001_project.databinding.ItemPropertyBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.screens.LoginActivity
import com.example.mads4001_project.screens.PropertyDetailActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.mads4001_project.utils.getLoggedInUser
import com.example.mads4001_project.utils.prefEditor
import com.example.mads4001_project.utils.saveDataToSharedPref
import com.example.mads4001_project.utils.sharedPreferences
import kotlin.math.log

class PropertyAdapter(private var properties: MutableList<Property>, private var loggedInUserName: String, private val showShortlistOnly: Boolean) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
//    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var prefEditor: SharedPreferences.Editor
    private var loggedInUser: User? = null
    private val shortlistedProperties: MutableList<String> = mutableListOf()
    private val tag = "Property Adapter"

    fun setProperties(newProperties: List<Property>) {
        val diffResult = DiffUtil.calculateDiff(PropertyDiffCallback(properties, newProperties))
        properties.clear()
        properties.addAll(newProperties)
        diffResult.dispatchUpdatesTo(this)
    }
    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property, context: Context, pos: Int) {
            Log.i(tag, "in bind ${loggedInUser}, ${this@PropertyAdapter.loggedInUser}")
//            // configure shared preferences
//            this@PropertyAdapter.sharedPreferences = context.getSharedPreferences("MY_APP_PREFS", AppCompatActivity.MODE_PRIVATE)
//            this@PropertyAdapter.prefEditor = this@PropertyAdapter.sharedPreferences.edit()

            Log.i(tag, "in adapter ${loggedInUser}")
            if(loggedInUserName != ""){
//                loggedInUser = Gson().fromJson(sharedPreferences.getString(loggedInUser?.username, ""), User::class.java)
                loggedInUser = getLoggedInUser(loggedInUserName, context)
                shortlistedProperties.clear()
                for (property in loggedInUser!!.shortlistedProperties){
                    shortlistedProperties.add(property.address)
                }
            }
            Log.i(tag, "short list ${shortlistedProperties}")

            // Assuming you want to display the property type as the title
            binding.propertyTitleTextView.text = property.type
            binding.propertyDescriptionTextView.text = property.description
            binding.propertyAddressTextView.text = property.address
            binding.propertyCityPostalTextView.text = "${property.city}, ${property.postalCode}"
            Glide.with(binding.root.context).load(property.imageUrl).into(binding.propertyImage)  // for online images

//            val imageName = property.imageUrl
//            Log.i(tag, "image url is $imageName")
//            val res = context.resources.getIdentifier(imageName, "drawable", context.packageName)
//            this.binding.propertyImage.setImageResource(res)

            Log.i(tag, "before change btn ${shortlistedProperties.contains(property.address)}, ${shortlistedProperties}, ${property}")
            if(loggedInUser != null && shortlistedProperties.contains(property.address)){
                binding.removeBtn.visibility = View.VISIBLE
                binding.shortListBtn.visibility = View.GONE
            }
            else if(loggedInUser != null){
                binding.shortListBtn.visibility = View.VISIBLE
                binding.removeBtn.visibility = View.GONE
            }
            else {
                binding.shortListBtn.visibility = View.GONE
                binding.removeBtn.visibility = View.GONE
            }


            binding.propertyCard.setOnClickListener {
                // popup property detail
                if(this@PropertyAdapter.loggedInUser != null){
                    Log.i(tag, "${loggedInUser} logged in")
                    val intent = Intent(context, PropertyDetailActivity::class.java)
                    intent.putExtra("PROPERTY", property)
                    context.startActivity(intent)
                }
                else {
                    Log.i(tag, "no one logged in")
                    Snackbar.make(binding.root, "Please login before viewing the property details.", Snackbar.LENGTH_LONG).show()

                    val intent = Intent(context, LoginActivity::class.java)
                    intent.putExtra("REFERER", "MainActivity")
                    context.startActivity(intent)
                }
            }

            binding.shortListBtn.setOnClickListener {
                Snackbar.make(binding.root, "add $pos to list", Snackbar.LENGTH_LONG).show()
                Log.i(tag, "from class shortbtn $loggedInUser")

                loggedInUser = Gson().fromJson(sharedPreferences.getString(loggedInUser?.username, ""), User::class.java)
                Log.i(tag, "from shared shortbtn $loggedInUser")

                Log.i(tag, "before added to list ${loggedInUser}")
                loggedInUser!!.shortlistedProperties.add(property)
                Log.i(tag, "after added to list ${loggedInUser}")
                saveDataToSharedPref(context, loggedInUser!!.username, loggedInUser!!, true)

                if(showShortlistOnly) {
                    properties = loggedInUser!!.shortlistedProperties
                }
                this@PropertyAdapter.notifyDataSetChanged()
            }

            binding.removeBtn.setOnClickListener {
                Snackbar.make(binding.root, "remove $pos to list", Snackbar.LENGTH_LONG).show()
                Log.i(tag, "from class removebtn $loggedInUser")

                loggedInUser = getLoggedInUser(loggedInUserName, context)
                Log.i(tag, "from shared removebtn $loggedInUser")

                Log.i(tag, "before remove $pos from list ${loggedInUser}")

                for(i in 0..< loggedInUser!!.shortlistedProperties.size){
                    if(loggedInUser!!.shortlistedProperties[i].equals(property)){
                        Log.i(tag, "removed property $property from ${loggedInUser!!.shortlistedProperties[i]}")
                        loggedInUser!!.shortlistedProperties.removeAt(i)
                        break
                    }
                }
//                loggedInUser!!.shortlistedProperties.removeAt(pos)
                Log.i(tag, "after remove $pos from list ${loggedInUser}")
                saveDataToSharedPref(context, loggedInUser!!.username, loggedInUser!!, true)

                if(showShortlistOnly){
                    properties = loggedInUser!!.shortlistedProperties
                }
                this@PropertyAdapter.notifyDataSetChanged()
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
        Log.i(tag, "on bind $property, $context, $position, $loggedInUser")

        holder.bind(property, context, position)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    fun updateProperties(newProperties: List<Property>) {
        properties.clear()
        properties.addAll(newProperties)
        notifyDataSetChanged()
    }

    private class PropertyDiffCallback(
        private val oldProperties: List<Property>,
        private val newProperties: List<Property>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldProperties.size
        override fun getNewListSize(): Int = newProperties.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldProperties[oldItemPosition] == newProperties[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldProperties[oldItemPosition] == newProperties[newItemPosition]
    }
}
