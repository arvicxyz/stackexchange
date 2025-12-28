package com.startapplab.stackexchange.data.repository

import com.startapplab.stackexchange.data.api.StackExchangeApi
import com.startapplab.stackexchange.data.api.UserDto
import com.startapplab.stackexchange.domain.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: StackExchangeApi
) {
    
    suspend fun getUsers(page: Int = 1, pageSize: Int = 20): Result<List<User>> {
        return try {
            val response = api.getUsers(page = page, pageSize = pageSize)
            val users = response.items.map { it.toDomainModel() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchUsers(query: String, page: Int = 1, pageSize: Int = 20): Result<List<User>> {
        return try {
            if (query.isBlank()) {
                return getUsers(page, pageSize)
            }
            val response = api.searchUsers(inname = query, page = page, pageSize = pageSize)
            val users = response.items.map { it.toDomainModel() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun UserDto.toDomainModel(): User {
        return User(
            id = userId,
            username = displayName,
            reputation = reputation,
            location = location,
            creationDate = creationDate?.let { formatDate(it) }
        )
    }
    
    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}
