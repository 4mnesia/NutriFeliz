package com.example.nutriapp.model

data class User(
    val fullName: String,
    val username: String,
    val email: String,
    val passwordHash: String
)
