package com.chandra.expensetracker.retrofit

import okhttp3.ResponseBody

interface AuthRepository {
    suspend fun login(): NetworkResult<ResponseBody>
}
