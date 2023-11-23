package com.example.mads4001_project.screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.MainActivity
import com.example.mads4001_project.adapters.LandlordPropertyAdapter
import com.example.mads4001_project.databinding.ActivityLandlordPropertiesBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.utils.saveDataToSharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LandlordPropertiesActivity : MainActivity() {
    lateinit var binding: ActivityLandlordPropertiesBinding
    lateinit var adapter: LandlordPropertyAdapter
    private var datasource: MutableList<Property> = mutableListOf<Property>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    private var loggedInUserName: String = ""
    override val tag = "Landlord"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandlordPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("PROPERTIES", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()


        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.title = "Your Property List"

        loggedInUserName = this.intent.getStringExtra("USER")!!
        val landlordPropertiesJson = sharedPreferences.getString(loggedInUserName, "")
        val landlordProperties = if(landlordPropertiesJson != "") Gson().fromJson<List<Property>>(landlordPropertiesJson, object : TypeToken<List<Property>>() {}.type) else mutableListOf()
        datasource.addAll(landlordProperties)
        Log.i(tag, "datasource is ${datasource}")

        adapter = LandlordPropertyAdapter(
            datasource,
            { pos -> rowClicked(pos) },
            { pos -> deleteProperty(pos) },
            { pos -> editClicked(pos)}
        )

        binding.returnBtn.setOnClickListener {
            finish()
        }

        binding.addPropertyBtn.setOnClickListener {
            val intent = Intent(this, AddPropertyActivity::class.java)
            intent.putExtra("USER", loggedInUserName)
            startActivity(intent)
        }

        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

    }


    private fun rowClicked(position: Int) {
        val selectedProperty: Property = datasource[position]
        val intent = Intent(this, PropertyDetailActivity::class.java)

        intent.putExtra("PROPERTY", selectedProperty)
        startActivity(intent)
    }

    private fun editClicked(position: Int) {
        val selectedProperty: Property = datasource[position]
        val intent = Intent(this, AddPropertyActivity::class.java)
        intent.putExtra("USER", loggedInUserName)
        intent.putExtra("PROPERTY_DATA", selectedProperty)
        intent.putExtra("INDEX", position)

        startActivity(intent)
    }


    private fun deleteProperty(position: Int) {
        datasource.removeAt(position)
        saveDataToSharedPref(this, "PROPERTIES", loggedInUserName, datasource, true )
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        val propertiesListFromSP = sharedPreferences.getString(loggedInUserName, "")
        if (propertiesListFromSP != "") {
            val gson = Gson()
            val typeToken = object : TypeToken<List<Property>>() {}.type
            val propertiesList = gson.fromJson<List<Property>>(propertiesListFromSP, typeToken)

            datasource.clear()
            datasource.addAll(propertiesList)
            adapter.notifyDataSetChanged()
        }

    }
}