package com.example.cafecornerapp.Domain

import java.io.Serializable

data class LaporanModel(
    val cartItems: List<LaporanProductModel>
) : Serializable
