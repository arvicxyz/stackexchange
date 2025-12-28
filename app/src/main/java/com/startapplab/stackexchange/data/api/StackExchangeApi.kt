package com.startapplab.stackexchange.data.api

import com.startapplab.stackexchange.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface StackExchangeApi {
    
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "asc",
        @Query("sort") sort: String = "name",
        @Query("site") site: String = "stackoverflow",
        @Query("key") key: String = API_KEY
    ): UsersResponse
    
    @GET("users")
    suspend fun searchUsers(
        @Query("inname") inname: String,
        @Query("page") page: Int = 1,
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "asc",
        @Query("sort") sort: String = "name",
        @Query("site") site: String = "stackoverflow",
        @Query("key") key: String = API_KEY
    ): UsersResponse
    
    companion object {
        const val BASE_URL = "https://api.stackexchange.com/2.3/"
        val API_KEY: String = BuildConfig.STACKEXCHANGE_API_KEY
    }
}
