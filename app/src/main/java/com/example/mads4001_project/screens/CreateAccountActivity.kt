package com.example.mads4001_project.screens

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import com.example.mads4001_project.R
import com.example.mads4001_project.adapters.PropertyAdapter
import com.example.mads4001_project.databinding.ActivityCreateAccountBinding
import com.example.mads4001_project.databinding.ActivityLoginBinding
import com.example.mads4001_project.models.Property
import com.example.mads4001_project.models.User
import com.example.mads4001_project.utils.prefEditor
import com.example.mads4001_project.utils.saveDataToSharedPref
import com.example.mads4001_project.utils.sharedPreferences
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class CreateAccountActivity : LoginActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    override val tag = "Create Account"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener {
            val username: String = this.binding.usernameInput.text.toString()
            val password: String = this.binding.passwordInput.text.toString()
            val confirmPassword: String = this.binding.confirmPasswordInput.text.toString()
            val checkBtn: RadioButton? = findViewById<RadioButton>(this.binding.userTypeRadioGp.checkedRadioButtonId)
            val userType = checkBtn?.text.toString()

            Log.i(tag, "new user ${username}, ${password}, ${confirmPassword}, ${userType}")

            // configure shared preferences
            this.sharedPreferences = getSharedPreferences("USERS",
                AppCompatActivity.MODE_PRIVATE
            )

            // check if the username has already been used
            val userJson = sharedPreferences.getString(username, "")
            val userAlreadyExist = if(userJson == "") false else true
            Log.i(tag, "user exist ? ${userJson} ${userAlreadyExist}")

            if(username == "") {
                Snackbar.make(binding.root, "Please enter a user name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(userType == "" || userType == "null" || userType == null) {
                Snackbar.make(binding.root, "Please select a user type", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password == "" || confirmPassword == "") {
                Snackbar.make(binding.root, "Please enter a password and the confirm password", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(userAlreadyExist) {
                Snackbar.make(binding.root, "User already exist!", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // check if the confirm password matches the password
            if(confirmPassword != password) {
                Snackbar.make(binding.root, "Confirm Password does not match the password", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            signup(username, password, userType)
        }
    }

    private fun signup(username: String, password: String, userType: String){
        Log.i(tag, "creating account $username, $password $userType")
        val newUser = User(username, password, userType)

        saveDataToSharedPref(this, "USERS", username, newUser, true)

        login(newUser)
    }
}