package com.example.mads4001_project

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.models.Owner
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.screens.AddPropertyActivity
import com.example.mads4001_project.screens.LoginActivity
import com.example.mads4001_project.screens.ShortlistActivity
import com.example.mads4001_project.utils.getLoggedInUser
import com.example.mads4001_project.utils.prefEditor
import com.example.mads4001_project.screens.LandlordPropertiesActivity
import com.example.mads4001_project.utils.getAllLandlordProperties
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var propertiesToBeDisplayed: MutableList<Property> = mutableListOf()
    private var allProperties: MutableList<Property> = mutableListOf()
//    private var loggedInUserName: String = ""
    private var loggedInUser: User? = null
    open val tag = "Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tag, "oncreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allProperties = initializeProperties(this)
        loggedInUser = getLoggedInUser(this)

        Log.i(tag, "property to be displayed ${loggedInUser}")

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        propertiesToBeDisplayed.addAll(allProperties)
        Log.i(tag, "on create ${loggedInUser}")
        propertyAdapter = PropertyAdapter(propertiesToBeDisplayed, loggedInUser?.username ?: "", false)

        binding.propertiesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.propertiesRecyclerView.adapter = propertyAdapter

        binding.searchButton.setOnClickListener {
            performSearch(binding.searchEditText.text.toString())
        }

        binding.condoImage.setOnClickListener {
            triggerSearchWithType("Condo")
        }

        binding.houseImage.setOnClickListener {
            triggerSearchWithType("House")
        }

        binding.apartmentImage.setOnClickListener {
            triggerSearchWithType("Apartment")
        }
    }

    // Function to trigger search with type
    private fun triggerSearchWithType(type: String) {
        binding.searchEditText.setText(type)
        performSearch(type)
    }

    override fun onResume() {
        Log.i(tag, "onresume")

        loggedInUser = getLoggedInUser(this)
//        if(loggedInUser != null){
//            propertyAdapter.notifyDataSetChanged()
//        }
        allProperties.clear()
        allProperties = initializeProperties(this)
        propertiesToBeDisplayed.clear()
        propertiesToBeDisplayed.addAll(allProperties)
        propertyAdapter.notifyDataSetChanged()

        super.onResume()
    }

    // options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu, menu)
        if(loggedInUser == null || loggedInUser?.userType == "Landlord") {
            menu.findItem(R.id.go_to_shortlist).setVisible(false)
        }
        if(loggedInUser?.userType == "Tenant") {
            menu.findItem(R.id.add_property).setVisible(false)
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
            R.id.add_property -> {
                if(loggedInUser != null) {
                    val intent = Intent(this, LandlordPropertiesActivity::class.java)
                    Log.i(tag, "send to add properties ${loggedInUser}")
                    intent.putExtra("USER", loggedInUser?.username)
                    startActivity(intent)
                    return true
                }
                else {
                    val intent = Intent(this, LoginActivity::class.java)
                    Log.i(tag, "send to add properties (login needed) ${loggedInUser}")
                    intent.putExtra("REFERER", "MainActivity")
                    startActivity(intent)
                    return true
                }
            }
            // for testing only. Remove this later
            R.id.delete_users -> {
                this.sharedPreferences = getSharedPreferences("USERS", MODE_PRIVATE)
                this.prefEditor = this.sharedPreferences.edit()

                prefEditor.clear()
                prefEditor.apply()
                Snackbar.make(binding.root, "Data erased!", Snackbar.LENGTH_LONG).show()
                return true
            }
            R.id.delete_properties -> {
                this.sharedPreferences = getSharedPreferences("PROPERTIES", MODE_PRIVATE)
                this.prefEditor = this.sharedPreferences.edit()

                prefEditor.clear()
                prefEditor.apply()
                Snackbar.make(binding.root, "property erased!", Snackbar.LENGTH_LONG).show()
                return true
            }
            else -> {
                Log.i(tag, "delete this, item is $item")
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun performSearch(query: String) {
        Log.i(tag, "all prop $allProperties")
        val filteredProperties = if(query != "") allProperties.filter { property ->
            property.matchesQuery(query)
        } else allProperties
        Log.i(tag, "filtered props $filteredProperties")
        propertiesToBeDisplayed.clear()
        propertiesToBeDisplayed.addAll(filteredProperties)
        propertyAdapter.notifyDataSetChanged()
    }


    private fun initializeProperties(context: Context): MutableList<Property> {
        val propertiesToBeDisplay = mutableListOf<Property>()
        val sampleOwner = Owner("John Doe", "johndoe@example.com", "+123456789")

        val sampleProperties = mutableListOf(
            Property(
                type = "Condo",
                owner = sampleOwner,
                description = "Modern condo with 2 bedrooms and a great view of the city.",
                numOfBedrooms = 2,
                numOfKitchens = 1,
                numOfBathrooms = 2,
                address = "123 Main St, Metropolis",
                city = "Metropolis",
                postalCode = "12345",
                availableForRent = true,
                imageUrl = "https://thumbor.forbes.com/thumbor/fit-in/1290x/https://www.forbes.com/advisor/wp-content/uploads/2022/10/condo-vs-apartment.jpeg.jpg"
            ),
            Property(
                type = "House",
                owner = sampleOwner,
                description = "Spacious house with a large backyard and modern amenities.",
                numOfBedrooms = 4,
                numOfKitchens = 1,
                numOfBathrooms = 3,
                address = "456 Maple Ave, Springfield",
                city = "Springfield",
                postalCode = "67890",
                availableForRent = true,
                imageUrl = "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/6fb8604d0f16bf8c22ea266d19bc7ccf-full.webp"
            ),
            Property(
                type = "Apartment",
                owner = sampleOwner,
                description = "Cozy apartment close to downtown and public transportation.",
                numOfBedrooms = 1,
                numOfKitchens = 1,
                numOfBathrooms = 1,
                address = "789 River Rd, Riverdale",
                city = "Riverdale",
                postalCode = "10111",
                availableForRent = false,
                imageUrl = "https://www.trulia.com/pictures/thumbs_6/zillowstatic/fp/5aa7dd68c2f6536683aaac7ad6f9b99d-full.webp"
            ),
            Property(
            type = "House",
            owner = sampleOwner,
            description = "Cozy suburban house with a beautiful garden and a family-friendly neighborhood.",
            numOfBedrooms = 3,
            numOfKitchens = 1,
            numOfBathrooms = 2,
            address = "202 Maple Lane, Pleasantville",
            city = "Pleasantville",
            postalCode = "30002",
            availableForRent = false,
            imageUrl = "https://www.bankrate.com/2022/07/20093642/what-is-house-poor.jpg?auto=webp&optimize=high&crop=16:9&width=912"
            ),
            Property(
            type = "Apartment",
            owner = sampleOwner,
            description = "Stylish downtown apartment with easy access to nightlife and public transport.",
            numOfBedrooms = 2,
            numOfKitchens = 1,
            numOfBathrooms = 1,
            address = "303 City Center Ave, Metropolis",
            city = "Metropolis",
            postalCode = "40003",
            availableForRent = true,
            imageUrl = "https://pqnk.com/wp-content/uploads/2021/07/SW-perspective-50-percent-transparency.png"
            ),
            Property(
            type = "House",
            owner = sampleOwner,
            description = "Traditional house in a historical neighborhood, featuring a large porch and backyard.",
            numOfBedrooms = 4,
            numOfKitchens = 1,
            numOfBathrooms = 3,
            address = "404 Heritage Road, Oldtown",
            city = "Oldtown",
            postalCode = "50004",
            availableForRent = true,
            imageUrl = "https://www.zolo.ca/blog/wp-content/uploads/2018/03/large-home-with-vibrant-landscaping-1024x576.jpeg"
            ),
            Property(
            type = "Condo",
            owner = sampleOwner,
            description = "Modern waterfront condo with panoramic ocean views and high-end finishes.",
            numOfBedrooms = 2,
            numOfKitchens = 1,
            numOfBathrooms = 2,
            address = "505 Seaside Blvd, Bay City",
            city = "Bay City",
            postalCode = "60005",
            availableForRent = false,
            imageUrl = "https://blog.remax.ca/wp-content/uploads/sites/8/2019/02/condo-690x518.jpg?resize=600,450"
             ),
            Property(
            type = "Apartment",
            owner = sampleOwner,
            description = "Compact and efficient apartment in a vibrant and trendy neighborhood.",
            numOfBedrooms = 1,
            numOfKitchens = 1,
            numOfBathrooms = 1,
            address = "606 Downtown Ave, Night City",
            city = "Night City",
            postalCode = "70006",
            availableForRent = true,
            imageUrl = "https://wp-cpr.s3.amazonaws.com/uploads/2023/07/Creekwalk-Apartments.png?resize=2064,1376"
             ),
            Property(
            type = "House",
            owner = sampleOwner,
            description = "Spacious country house with a large garden and serene natural surroundings.",
            numOfBedrooms = 5,
            numOfKitchens = 1,
            numOfBathrooms = 4,
            address = "707 Countryside Lane, Greenfield",
            city = "Greenfield",
            postalCode = "80007",
            availableForRent = true,
            imageUrl = "https://i.pinimg.com/564x/1a/16/2c/1a162ca2f756d8417c6e8cc977bc7fbc.jpg"
        )

        )

        // show the properties added by the landlords
        this.sharedPreferences = getSharedPreferences("PROPERTIES", MODE_PRIVATE)
//        val allLandlordProperties = sharedPreferences.getString("ALL_LANDLORD_PROPERTIES", "")
        val allLandlordProperties = getAllLandlordProperties(context)
//        val landlordProperties = if(allLandlordProperties != "") Gson().fromJson<List<Property>>(allLandlordProperties, object : TypeToken<List<Property>>() {}.type) else mutableListOf()
        Log.i(tag, "after get all ${allLandlordProperties}")
        propertiesToBeDisplay.addAll(sampleProperties)
        propertiesToBeDisplay.addAll(allLandlordProperties)
        return propertiesToBeDisplay
    }

}
