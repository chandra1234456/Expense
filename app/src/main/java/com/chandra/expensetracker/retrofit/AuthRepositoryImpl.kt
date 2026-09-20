package com.chandra.expensetracker.retrofit

import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(): NetworkResult<ResponseBody> {
        return try {
            val response = apiService.getData()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Error(response.code(), "Response body is empty")
                }
            } else {
                NetworkResult.Error(
                    response.code(),
                    response.errorBody()?.string() ?: response.message()
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}
