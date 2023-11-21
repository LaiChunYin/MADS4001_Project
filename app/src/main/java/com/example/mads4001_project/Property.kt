package com.example.mads4001_project

class Property(
    val type: String,
    val owner: Owner,
    val description: String,
    val numOfRooms: Int,
    val numOfBedrooms: Int,
    val numOfKitchens: Int,
    val numOfBathrooms: Int,
    val availableForRent: Boolean,
) {
}