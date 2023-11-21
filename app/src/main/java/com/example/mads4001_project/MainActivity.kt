package com.example.mads4001_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.models.Owner

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)
    }
}