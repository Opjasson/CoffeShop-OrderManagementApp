package com.example.cafecornerapp.Repository

import com.example.cafecornerapp.Helper.ConvertDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class TransaksiRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()

    //    Add cart
    fun createTransaksi(
        userId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "userId" to userId,
            "totalHarga" to totalHarga,
            "catatanTambahan" to catatanTambahan,
            "buktiTransfer" to buktiTransfer,
            "createdAt" to convertDate.formatTimestamp(Timestamp.now())
        )
        database.collection("transaksi")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}