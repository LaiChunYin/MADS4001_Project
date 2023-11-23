package com.example.mads4001_project.models

import java.io.Serializable

class Property(
    val address: String,
    val city: String,
    val postalCode: String,
    val type: String,
    val owner: Owner,
    val description: String,
    val numOfBedrooms: Int,
    val numOfKitchens: Int,
    val numOfBathrooms: Int,
    val availableForRent: Boolean,
    val imageUrl: String? = null
): Serializable {
    fun matchesQuery(query: String): Boolean {
        val lowerCaseQuery = query.lowercase()

        val matchFound = type.lowercase().contains(lowerCaseQuery) ||
                description.lowercase().contains(lowerCaseQuery) ||
                owner.name.lowercase().contains(lowerCaseQuery) ||
                address.lowercase().contains(lowerCaseQuery) ||
                city.lowercase().contains(lowerCaseQuery) ||
                postalCode.lowercase().contains(lowerCaseQuery) ||
                matchesNumericQuery(lowerCaseQuery)

        return matchFound
    }

    override fun toString(): String {
        return "Property is $address, $type, $description, $numOfKitchens, $numOfBedrooms, $numOfBathrooms, $availableForRent, $imageUrl"
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Property) return false
        if(this.postalCode != other.postalCode) return false
        if(this.address != other.address) return false

        return true
    }
    private fun matchesNumericQuery(query: String): Boolean {
        val queryAsNumber = query.toIntOrNull()
        return queryAsNumber != null && (
                numOfBedrooms == queryAsNumber ||
                numOfKitchens == queryAsNumber ||
                numOfBathrooms == queryAsNumber)
    }
}
