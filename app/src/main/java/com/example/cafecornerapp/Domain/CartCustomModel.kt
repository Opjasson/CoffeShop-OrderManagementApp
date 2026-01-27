package com.example.cafecornerapp.Domain

import java.io.Serializable

data class CartCustomModel(
    val cartId: String,
    val productId: String,
    val nama: String,
    val harga: Int,
    val kategori: String,
    val jumlah: Int,
    val imgUrl: String
) : Serializable
