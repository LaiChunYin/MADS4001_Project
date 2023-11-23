package com.example.mads4001_project.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.mads4001_project.models.User
import com.google.gson.Gson
import android.content.SharedPreferences
import android.util.Log
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
//            this.sharedPreferences = context.getSharedPreferences("MY_APP_PREFS",
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
fun getLoggedInUser(context: AppCompatActivity): User? {
    val loggedInUserName = context.intent.getStringExtra("USER") ?: ""
    Log.i("helper", "in getloggedInUser ${loggedInUserName}")
    if(loggedInUserName == "") return null
    // configure shared preferences
    sharedPreferences = context.getSharedPreferences("MY_APP_PREFS",
        AppCompatActivity.MODE_PRIVATE
    )
    return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
}

fun getLoggedInUser(loggedInUserName: String, context: Context): User? {
    if(loggedInUserName == "") return null
    // configure shared preferences
    sharedPreferences = context.getSharedPreferences("MY_APP_PREFS",
        AppCompatActivity.MODE_PRIVATE
    )
    return Gson().fromJson(sharedPreferences.getString(loggedInUserName, ""), User::class.java)
}

fun getDataFromSharedPref() {

}

fun saveDataToSharedPref(context: Context, key: String, data: Any, toJson: Boolean = false) {
    var dataString: String

    // configure shared preferences
    sharedPreferences = context.getSharedPreferences("MY_APP_PREFS",
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