package com.example.mads4001_project.screens

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityShortlistBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.utils.getLoggedInUser

class ShortlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShortlistBinding
    private var loggedInUser: User? = null
    private val tag = "Shortlist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.returnBtn.setOnClickListener {
            finish()
        }

        loggedInUser = getLoggedInUser(this)
        if(loggedInUser != null){
            var shortlistedProperties:MutableList<Property> = mutableListOf()
            if (loggedInUser != null) {
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