package com.example.mads4001_project


class Property(
    val type: String,
    val owner: Owner,
    val description: String,
    val numOfRooms: Int,
    val numOfBedrooms: Int,
    val numOfKitchens: Int,
    val numOfBathrooms: Int,
    val address: String,
    val city: String,
    val postalCode: String,
    val imageURL: String,
    val availableForRent: Boolean
) {
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


    private fun matchesNumericQuery(query: String): Boolean {
        val queryAsNumber = query.toIntOrNull()
        return queryAsNumber != null && (
                numOfRooms == queryAsNumber ||
                        numOfBedrooms == queryAsNumber ||
                        numOfKitchens == queryAsNumber ||
                        numOfBathrooms == queryAsNumber)
    }
}
