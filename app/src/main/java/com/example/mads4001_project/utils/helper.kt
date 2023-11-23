package com.example.mads4001_project.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.mads4001_project.models.User
import com.google.gson.Gson
import android.content.SharedPreferences
import android.util.Log
import com.example.mads4001_project.models.Property
import com.google.gson.reflect.TypeToken
import java.util.Objects

//class helper {
//    companion object {
//        private lateinit var sharedPreferences: SharedPreferences
//        private lateinit var prefEditor: SharedPreferences.Editor
//        fun getLoggedInUser(context: AppCompatActivity): User? {
//            val loggedInUserName = context.intent.getStringExtra("USER") ?: ""
//            if(loggedInUserName == "") return null
//
//            // configure shared preferences
//            this.sharedPreferences = context.getSharedPreferences("USERS",
//                AppCompatActivity.MODE_PRIVATE
//            )
//            this.prefEditor = this.sharedPreferences.edit()
//
//            return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
//        }
//    }
//}


lateinit var sharedPreferences: SharedPreferences
lateinit var prefEditor: SharedPreferences.Editor
val tag = "Utils"
fun getLoggedInUser(context: AppCompatActivity): User? {
    val loggedInUserName = context.intent.getStringExtra("USER") ?: ""
    Log.i("helper", "in getloggedInUser ${loggedInUserName}")
    if(loggedInUserName == "") return null
    // configure shared preferences
    sharedPreferences = context.getSharedPreferences("USERS",
        AppCompatActivity.MODE_PRIVATE
    )
    return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
}

fun getLoggedInUser(loggedInUserName: String, context: Context): User? {
    if(loggedInUserName == "") return null
    // configure shared preferences
    sharedPreferences = context.getSharedPreferences("USERS",
        AppCompatActivity.MODE_PRIVATE
    )
    return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
}

fun saveDataToSharedPref(context: Context, file: String, key: String, data: Any, toJson: Boolean = false) {
    var dataString: String

    // configure shared preferences
    sharedPreferences = context.getSharedPreferences(file,
        AppCompatActivity.MODE_PRIVATE
    )
    prefEditor = sharedPreferences.edit()

    if(toJson){
        dataString = Gson().toJson(data)
    }
    else {
        dataString = data.toString()
    }
    prefEditor.putString(key, dataString)
    prefEditor.apply()
}

fun getAllLandlordProperties(context: Context): MutableList<Property> {
    val allProperties = mutableListOf<Property>()
    sharedPreferences = context.getSharedPreferences("PROPERTIES", AppCompatActivity.MODE_PRIVATE)
    Log.i(tag, "getting all land ${sharedPreferences.all.isEmpty()} $sharedPreferences")

    // delete this
    if(sharedPreferences.all.isNotEmpty()) {
//        for (key in sharedPreferences.all.keys) {
//            Log.i(tag, "key is ${key}")
//        }
        val gson = Gson()
        for(properties in sharedPreferences.all.values){
            Log.i(tag, "key prop is ${properties!!::class.simpleName} ${properties}")
            val landlordProperties = gson.fromJson<List<Property>>(properties as String, object : TypeToken<List<Property>>() {}.type)
            allProperties.addAll(landlordProperties)
        }

    }
    Log.i(tag, "all properties is ${allProperties}")
    return allProperties
}

fun checkDuplicatedProperty(newProperty: Property, context: Context): Boolean {
    val allProperty = getAllLandlordProperties(context)
    for(property in allProperty){
        Log.i(tag, "checking ${property}, ${newProperty}")
        if(newProperty.equals(property)) return true
    }
    return false

}