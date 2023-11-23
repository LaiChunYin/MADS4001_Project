package com.example.mads4001_project.models

import android.util.Log
import java.io.Serializable

class Property(
    val address: String,
    val type: String,
    val owner: Owner,
    val description: String,
    val numOfRooms: Int,
    val numOfBedrooms: Int,
    val numOfKitchens: Int,
    val numOfBathrooms: Int,
    val availableForRent: Boolean,
    val imageUrl: String? = null
): Serializable {
    fun matchesQuery(query: String): Boolean {
        // Convert the query to lowercase for case-insensitive comparison
        val lowerCaseQuery = query.lowercase()

        // Check if the query matches any of the property details
        // Here, we check against type, description, and the owner's name
        if (type.lowercase().contains(lowerCaseQuery) ||
            description.lowercase().contains(lowerCaseQuery) ||
            owner.name.lowercase().contains(lowerCaseQuery)) {
            return true
        }

        // Check if the query is a number and matches the number of rooms/bedrooms/kitchens/bathrooms
        val queryAsNumber = query.toIntOrNull()
        if (queryAsNumber != null) {
            if (numOfRooms == queryAsNumber ||
                numOfBedrooms == queryAsNumber ||
                numOfKitchens == queryAsNumber ||
                numOfBathrooms == queryAsNumber) {
                return true
            }
        }

        // If none of the fields match, return false
        return false
    }

    override fun toString(): String {
        return "Property is $address, $type, $description, $numOfKitchens, $numOfBedrooms, $numOfBathrooms, $numOfRooms, $availableForRent, $imageUrl"
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Property) return false
        if(this.address != other.address) return false

        return true
    }
}