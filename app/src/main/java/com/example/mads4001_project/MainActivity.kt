package com.example.mads4001_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.databinding.ActivityMainBinding

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
        Log.d("MainActivity", "Searching for: $query")
        val filteredProperties = properties.filter { property ->
            property.matchesQuery(query)
        }
        Log.d("MainActivity", "Found ${filteredProperties.size} properties")
        propertyAdapter.updateProperties(filteredProperties)
    }


    private fun initializeProperties(): MutableList<Property> {
        val sampleOwner = Owner("John Doe", "johndoe@example.com", "+123456789")

        val sampleProperties = mutableListOf(
            Property(
                type = "Condo",
                owner = sampleOwner,
                description = "Modern condo with 2 bedrooms and a great view of the city.",
                numOfRooms = 5,
                numOfBedrooms = 2,
                numOfKitchens = 1,
                numOfBathrooms = 2,
                address = "123 Main St, Metropolis",
                city = "Metropolis",
                postalCode = "12345",
                availableForRent = true,
                imageURL = "https://thumbor.forbes.com/thumbor/fit-in/1290x/https://www.forbes.com/advisor/wp-content/uploads/2022/10/condo-vs-apartment.jpeg.jpg"  // Sample image URL
            ),
            Property(
                type = "House",
                owner = sampleOwner,
                description = "Spacious house with a large backyard and modern amenities.",
                numOfRooms = 7,
                numOfBedrooms = 4,
                numOfKitchens = 1,
                numOfBathrooms = 3,
                address = "456 Maple Ave, Springfield",
                city = "Springfield",
                postalCode = "67890",
                availableForRent = true,
                imageURL = "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/6fb8604d0f16bf8c22ea266d19bc7ccf-full.webp"  // Sample image URL
            ),
            Property(
                type = "Apartment",
                owner = sampleOwner,
                description = "Cozy apartment close to downtown and public transportation.",
                numOfRooms = 3,
                numOfBedrooms = 1,
                numOfKitchens = 1,
                numOfBathrooms = 1,
                address = "789 River Rd, Riverdale",
                city = "Riverdale",
                postalCode = "10111",
                availableForRent = false,
                imageURL = "https://www.trulia.com/pictures/thumbs_6/zillowstatic/fp/5aa7dd68c2f6536683aaac7ad6f9b99d-full.webp"  // Sample image URL
            )
            // Add more properties as needed
        )

        return sampleProperties
    }

}
