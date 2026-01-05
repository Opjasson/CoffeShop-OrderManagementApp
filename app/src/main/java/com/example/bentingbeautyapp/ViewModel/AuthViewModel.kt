package com.example.bentingbeautyapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bentingbeautyapp.Repository.AuthRepository

class AuthViewModel: ViewModel() {
    private val repository = AuthRepository()

//  Register Model
    private val _regisResult = MutableLiveData<Result<String>>()
    val registResult: LiveData<Result<String>> = _regisResult

    fun register(email : String, password : String){
        repository.registAuth(email, password) {

            success, message ->
            if(success) {
                _regisResult.value = Result.success("Register Berhasil")
            }else {
                _regisResult.value = Result.failure(Exception(message))
            }
        }
    }
}