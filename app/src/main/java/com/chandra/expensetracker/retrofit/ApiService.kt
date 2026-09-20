package com.chandra.expensetracker.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun getData(): Response<ResponseBody>
}