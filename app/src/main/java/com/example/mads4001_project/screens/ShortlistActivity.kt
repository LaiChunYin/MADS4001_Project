package com.example.mads4001_project.screens

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.R
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.databinding.ActivityShortlistBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.utils.getLoggedInUser
import com.google.gson.Gson

class ShortlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShortlistBinding
    private var loggedInUser: User? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private val tag = "Shortlist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // configure shared preferences
//        this.sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)
//        this.prefEditor = this.sharedPreferences.edit()

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.returnBtn.setOnClickListener {
            finish()
        }

        loggedInUser = getLoggedInUser(this)
        if(loggedInUser != null){
            var shortlistedProperties:MutableList<Property> = mutableListOf()
            if (loggedInUser != null) {
                Log.i(tag, "in short list $loggedInUser")
                shortlistedProperties = loggedInUser!!.shortlistedProperties
            }

            var adapter = PropertyAdapter(shortlistedProperties, loggedInUser?.username ?: "", true)
            this.binding.shortlistRv.adapter = adapter
            this.binding.shortlistRv.layoutManager = LinearLayoutManager(this)
            this.binding.shortlistRv.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

    }

}