package com.example.cafecornerapp.Domain

import java.io.Serializable

data class Users(
    var email : String = "",
    var username : String = ""
) : Serializable
