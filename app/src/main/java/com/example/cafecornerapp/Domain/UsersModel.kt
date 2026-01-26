package com.example.cafecornerapp.Domain

import java.io.Serializable

data class UsersModel(
    var documentId : String = "",
    var email : String = "",
    var username : String = ""
) : Serializable
