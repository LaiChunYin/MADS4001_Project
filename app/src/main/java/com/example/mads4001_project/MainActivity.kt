package com.example.mads4001_project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.models.Owner
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.screens.ShortlistActivity
import com.example.mads4001_project.utils.getLoggedInUser
import com.google.android.material.snackbar.Snackbar
import com.example.mads4001_project.utils.getLoggedInUser
import com.google.gson.Gson
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var properties: MutableList<Property> = mutableListOf()
//    private var loggedInUserName: String = ""
    private var loggedInUser: User? = null
    val tag = "Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tag, "oncreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loggedInUser = getLoggedInUser(this)

        Log.i(tag, "property to be displayed ${loggedInUser}")

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        properties = initializeProperties()
        Log.i(tag, "on create ${loggedInUser}")
        propertyAdapter = PropertyAdapter(properties, loggedInUser?.username ?: "", false)


        binding.propertiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = propertyAdapter
        }

        binding.searchButton.setOnClickListener {
            performSearch(binding.searchEditText.text.toString())
        }
    }

    override fun onResume() {
        Log.i(tag, "onresume")

        loggedInUser = getLoggedInUser(this)
        if(loggedInUser != null){
            propertyAdapter.notifyDataSetChanged()
        }

        super.onResume()
    }

    // options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(loggedInUser != null) {
            menuInflater.inflate(R.menu.options_menu, menu)
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.go_to_shortlist -> {
                val intent = Intent(this, ShortlistActivity::class.java)
                Log.i(tag, "send to shortlist ${loggedInUser}")
                intent.putExtra("USER", loggedInUser?.username)
                startActivity(intent)
                return true
            }
            // for testing only. Remove this later
            R.id.delete_users -> {
                prefEditor.clear()
                prefEditor.apply()
                Snackbar.make(binding.root, "Data erased!", Snackbar.LENGTH_LONG).show()
                return true
            }
            else -> {
                Log.i(tag, "delete this, item is $item")
                super.onOptionsItemSelected(item)
            }
        }
    }



    private fun performSearch(query: String) {
        val filteredProperties = properties.filter { property ->
            property.matchesQuery(query)
        }
        propertyAdapter.updateProperties(filteredProperties)
    }

    private fun initializeProperties(): MutableList<Property> {
        // Sample owner
        val sampleOwner = Owner("John Doe", "johndoe@example.com", "+123456789")

        // Sample list of properties
        val sampleProperties = mutableListOf(
            Property(
                type = "Condo",
                owner = sampleOwner,
                description = "Modern condo with 2 bedrooms and a great view of the city.",
                numOfRooms = 5,
                numOfBedrooms = 2,
                numOfKitchens = 1,
                numOfBathrooms = 2,
                address = "123 Road",
                availableForRent = true,
                imageUrl = "condo"
            ),
            Property(
                type = "House",
                owner = sampleOwner,
                description = "Spacious house with a large backyard and modern amenities.",
                numOfRooms = 7,
                numOfBedrooms = 4,
                numOfKitchens = 1,
                numOfBathrooms = 3,
                address = "456 Road",
                availableForRent = true,
                imageUrl = "house"
            ),
            Property(
                type = "Apartment",
                owner = sampleOwner,
                description = "Cozy apartment close to downtown and public transportation.",
                numOfRooms = 3,
                numOfBedrooms = 1,
                numOfKitchens = 1,
                numOfBathrooms = 1,
                address = "789 Road",
                availableForRent = false,
                imageUrl = "apartment"
            )
            // Add more properties as needed
        )

        return sampleProperties
    }


//    fun getLoggedInUser(): User? {
//        val loggedInUserName = this.intent.getStringExtra("USER") ?: ""
//        if(loggedInUserName == "") return null
//
//        // configure shared preferences
//        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
//        this.prefEditor = this.sharedPreferences.edit()
//
//        return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
//    }
}
