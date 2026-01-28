package com.example.cafecornerapp.Repository

import androidx.lifecycle.viewModelScope
import com.example.cafecornerapp.DataStore.TransaksiPreference
import com.example.cafecornerapp.Domain.CartModel
import com.example.cafecornerapp.Domain.ProductModel
import com.example.cafecornerapp.Domain.TransaksiModel
import com.example.cafecornerapp.Helper.ConvertDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    //    Get cart by transaksiId to handle history transaksi
    suspend fun getCartHistoryTransaksi(transaksiId: String): List<CartModel> {
        val snapshot = database.collection("cart")
            .whereEqualTo("transaksiId", transaksiId)
            .get()
            .await()

        return snapshot.toObjects(CartModel::class.java)
    }

//    Get transaksi user
suspend fun getTransaksiByUser(userId: String): List<Pair<String, TransaksiModel>> {
    val snapshot = database.collection("transaksi")
        .whereEqualTo("userId", userId)
        .get()
        .await()

    return snapshot.documents.mapNotNull { doc ->
        doc.toObject(TransaksiModel::class.java)?.let {
            doc.id to it
        }
    }
}

    //    get product by productId to history transaksi
    suspend fun getProductById(productId: String): ProductModel? {
        val doc = database.collection("product")
            .document(productId)
            .get()
            .await()

        return doc.toObject(ProductModel::class.java)
    }
}