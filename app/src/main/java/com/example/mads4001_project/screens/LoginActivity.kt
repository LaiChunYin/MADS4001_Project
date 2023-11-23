package com.example.mads4001_project.screens

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mads4001_project.MainActivity
import com.example.mads4001_project.R
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityLoginBinding
import com.example.mads4001_project.databinding.ActivityMainBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

open class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    open val tag = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure shared preferences
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val isFromMain = this@LoginActivity.intent.extras != null &&
                        this@LoginActivity.intent.extras!!.containsKey("REFERER") &&
                        this@LoginActivity.intent.getStringExtra("REFERER") == "MainActivity"
        if(isFromMain){
            Log.i(tag, "login needed")
            this@LoginActivity.intent.removeExtra("REFERER")
            Snackbar.make(binding.root, "Please login before viewing the property details", Snackbar.LENGTH_LONG).show()
        }

        binding.loginBtn.setOnClickListener {
            Log.i(tag, "login clicked")
            val username = this.binding.usernameInput.text.toString()
            val password = this.binding.passwordInput.text.toString()

            val userJson = sharedPreferences.getString(username, "")
            val user = Gson().fromJson(userJson, User::class.java)

            if(user == null || password != user.password) {
                Snackbar.make(binding.root, "Invalid username and password", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            login(user)
        }

        binding.signUpBtn.setOnClickListener {
            Log.i(tag, "signup clicked")

            val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    protected fun login(user: User){
        Log.i(tag, "login checking all pass")

//        val userJson = sharedPreferences.getString(user.username, "")
//        val user = Gson().fromJson(userJson, User::class.java)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("USER", user.username)
        startActivity(intent)
    }

}