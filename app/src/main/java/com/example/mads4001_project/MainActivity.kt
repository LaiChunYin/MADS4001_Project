package com.example.mads4001_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.models.Owner
import com.example.mads4001_project.models.Property

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private var properties: MutableList<Property> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        properties = initializeProperties()

        propertyAdapter = PropertyAdapter(properties)
        binding.propertiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = propertyAdapter
        }

        binding.searchButton.setOnClickListener {
            performSearch(binding.searchEditText.text.toString())
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
                availableForRent = true
            ),
            Property(
                type = "House",
                owner = sampleOwner,
                description = "Spacious house with a large backyard and modern amenities.",
                numOfRooms = 7,
                numOfBedrooms = 4,
                numOfKitchens = 1,
                numOfBathrooms = 3,
                availableForRent = true
            ),
            Property(
                type = "Apartment",
                owner = sampleOwner,
                description = "Cozy apartment close to downtown and public transportation.",
                numOfRooms = 3,
                numOfBedrooms = 1,
                numOfKitchens = 1,
                numOfBathrooms = 1,
                availableForRent = false
            )
            // Add more properties as needed
        )

        return sampleProperties
    }

}
