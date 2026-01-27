package com.example.cafecornerapp.Repository

import androidx.lifecycle.viewModelScope
import com.example.cafecornerapp.DataStore.TransaksiPreference
import com.example.cafecornerapp.Helper.ConvertDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.UUID


class TransaksiRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()


    //    Add cart
    fun createTransaksi(
        userId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
        onResult: (String) -> Unit
    ) {
        val transactionId = UUID.randomUUID().toString()
        var data = mapOf(
            "userId" to userId,
            "totalHarga" to totalHarga,
            "catatanTambahan" to catatanTambahan,
            "buktiTransfer" to buktiTransfer,
            "createdAt" to convertDate.formatTimestamp(Timestamp.now())
        )
        database.collection("transaksi")
            .document(transactionId)
            .set(data)
            .addOnSuccessListener {
                onResult(transactionId)
            }
            .addOnFailureListener {
                onResult("")
            }
    }

    //    Update Transaksi
    fun updateTransaksi(
        transaksiId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "totalHarga" to totalHarga,
            "catatanTambahan" to catatanTambahan,
            "buktiTransfer" to buktiTransfer,
        )
        database.collection("transaksi")
            .document(transaksiId)
            .update(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}