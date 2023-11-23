package com.example.mads4001_project.models

import java.io.Serializable
import java.security.cert.CertPathValidatorException

open class User(
    val username: String,
    val password: String,
    val userType: String,
    val shortlistedProperties: MutableList<Property> = mutableListOf()
): Serializable {
    override fun toString(): String {
        return "User is ($username, $password, $userType, $shortlistedProperties)"
    }
}