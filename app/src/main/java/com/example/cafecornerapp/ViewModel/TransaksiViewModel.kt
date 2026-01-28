package com.example.cafecornerapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafecornerapp.DataStore.TransaksiPreference
import com.example.cafecornerapp.Domain.CartCustomModel
import com.example.cafecornerapp.Domain.TransaksiWithCartModel
import com.example.cafecornerapp.Repository.TransaksiRepository
import kotlinx.coroutines.launch


class TransaksiViewModel : ViewModel() {
    private val repository = TransaksiRepository()
//    private var prefRepo = TransaksiPreference



    //    Create item
    val createStatus = MutableLiveData<String>()

    fun createTransaksi(
        userId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
    ) {
        repository.createTransaksi(
            userId,
            totalHarga,
            catatanTambahan,
            buktiTransfer) {
                success ->
            if (!success.isEmpty()){
                createStatus.value = success
            }else {
                Log.d("FAILEDCREATE", "FAILED-CREATE ITEM")
            }
        }
    }

    //    Update Transaksi
    val updateStatus = MutableLiveData<Boolean>()

    fun updateTransaksi(
        transaksiId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
    ) {
        repository.updateTransaksi(transaksiId, totalHarga, catatanTambahan, buktiTransfer) {
            updateStatus.value = it
        }
    }

//    get transaksi to handle history transaksi
private val _transaksiUI =
    MutableLiveData<List<TransaksiWithCartModel>>()

    val transaksiUI: LiveData<List<TransaksiWithCartModel>> =
        _transaksiUI

fun loadTransaksiWithCart(userId: String) {
    viewModelScope.launch {
        val transaksiList = repository.getTransaksiByUser(userId)

        val result = transaksiList.map { (transaksiId, transaksi) ->

            val carts = repository.getCartHistoryTransaksi(transaksiId)

            val cartCustom = carts.mapNotNull { cart ->
                val product = repository.getProductById(cart.productId)
                product?.let {
                    CartCustomModel(
                        productId = cart.productId,
                        nama = it.nama_product,
                        harga = it.harga_product,
                        kategori = it.kategori_product,
                        jumlah = cart.jumlah
                    )
                }
            }

            TransaksiWithCartModel(
                transaksiId = transaksiId,
                transaksi = transaksi,
                cartItems = cartCustom
            )
        }

        _transaksiUI.postValue(result)
    }
}
}