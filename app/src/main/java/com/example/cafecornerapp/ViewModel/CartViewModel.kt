package com.example.cafecornerapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafecornerapp.Domain.CartCustomModel
import com.example.cafecornerapp.Domain.CartModel
import com.example.cafecornerapp.Domain.ProductModel
import com.example.cafecornerapp.Repository.CartRepository
import com.example.cafecornerapp.Repository.ProductRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repository = CartRepository()

    private val repositoryProduct = ProductRepository()

    //    Create item
    val createStatus = MutableLiveData<Boolean>()

    fun addCart(
        userId: String,
        transaksiId: String,
        productId: String,
        jumlah: Long
    ) {
        Log.d("DATAKU", "dipanggil $userId $transaksiId $productId")
        repository.addCart(
            userId,
            transaksiId,
            productId,
            jumlah) {
                success ->
            if (success){
                createStatus.value = success
            }else {
                Log.d("FAILEDCREATE", "FAILED-CREATE ITEM")
            }
        }
    }

    //    get product by kategori
    private val _cartResult = MutableLiveData<List<CartModel>>()
    val cartResult: LiveData<List<CartModel>> = _cartResult

    fun getCartByTransaksiId(transaksiId : String) {
        repository.getCartByTransaksiId(transaksiId) {
            _cartResult.value = it
        }
    }

    //    get transaksi custom
    private val _transaksiUI = MutableLiveData<List<CartCustomModel>>()
    val transaksiUI: LiveData<List<CartCustomModel>> = _transaksiUI

    fun loadCartCustom(transaksiList: List<CartModel>) {
        viewModelScope.launch {
            val result = transaksiList.map { transaksi ->
                val product = repositoryProduct.getProductByProductId(transaksi.productId)

                    CartCustomModel(
                        cartId = transaksi.documentId,
                        productId = transaksi.productId,
                        nama = product!!.nama_product,
                        harga = product!!.harga_product.toString().toInt(),
                        kategori = product!!.kategori_product,
                        jumlah = transaksi!!.jumlah.toString().toInt(),
                        imgUrl = product!!.imgUrl.toString()
                    )

            }
            _transaksiUI.postValue(result)
        }
    }

    //    Delete product
    var _successDelete = MutableLiveData<Boolean>()

    fun deleteCart(cartId : String) {
        repository.deleteCart(cartId) {
            _successDelete.value = it
        }
    }
}