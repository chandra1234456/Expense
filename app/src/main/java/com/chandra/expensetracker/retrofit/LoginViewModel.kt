package com.chandra.expensetracker.retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun performApiCheck() {
        viewModelScope.launch {
            when (val result = authRepository.login()) {
                is NetworkResult.Success -> {
                    Log.d("LoginViewModel", "API Response success: ${result.data.string()}")
                }
                is NetworkResult.Error -> {
                    Log.e("LoginViewModel", "API HTTP Error code ${result.code}: ${result.message}")
                }
                is NetworkResult.Exception -> {
                    Log.e("LoginViewModel", "API Exception occurred: ${result.e.message}", result.e)
                }
            }
        }
    }
}