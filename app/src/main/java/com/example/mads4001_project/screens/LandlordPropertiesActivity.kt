package com.example.mads4001_project.screens

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.MainActivity
import com.example.mads4001_project.R
import com.example.mads4001_project.adapters.LandlordPropertyAdapter
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityLandlordPropertiesBinding
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.screens.AddPropertyActivity
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.screens.PropertyDetailActivity
import com.example.mads4001_project.utils.getLoggedInUser
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
    private var loggedInUser: User? = null
    override var tag = "Landlord"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandlordPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.sharedPreferences = getSharedPreferences("PROPERTIES", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()


        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.title = "Your Property List"

        loggedInUserName = this.intent.getStringExtra("USER")!!
        Log.i(tag, "loggedinusername is ${loggedInUserName}")
        val landlordPropertiesJson = sharedPreferences.getString(loggedInUserName, "")
        Log.i(tag, "json is ${landlordPropertiesJson}")
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
            Log.i(tag, "loggin pass to add prop ${loggedInUserName}")
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
//        saveDataToSharedPref(this, "KEY_PROPERTIES_DATASOURCE", datasource, true )
//        saveDataToSharedPref(this, "PROPERTIES", loggedInUserName, datasource, true )
        saveDataToSharedPref(this, "PROPERTIES", loggedInUserName, datasource, true )
//        saveDataToSharedPref(this, "PROPERTIES", "ALL_LANDLORD_PROPERTIES", allLandlordProperties, true )
//        val gson = Gson()
//        val listAsString = gson.toJson(datasource)
//        this.prefEditor.putString("KEY_PROPERTIES_DATASOURCE", listAsString)
//        this.prefEditor.apply()
        adapter.notifyDataSetChanged()
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.options_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.add_property -> {
//                val intent = Intent(this, AddPropertyActivity::class.java)
//                startActivity(intent)
//                return true
//            }
//            R.id.delete_properties -> {
//                deleteProperties()
//                return true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

//    private fun deleteProperties() {
//        prefEditor.remove("KEY_PROPERTIES_DATASOURCE")
//
//        prefEditor.apply()
//
//        datasource.clear()
//        adapter.notifyDataSetChanged()
//    }

    override fun onResume() {
        super.onResume()

//        val propertiesListFromSP = sharedPreferences.getString("KEY_PROPERTIES_DATASOURCE", "")
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