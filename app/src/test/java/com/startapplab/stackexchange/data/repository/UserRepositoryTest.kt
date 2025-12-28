package com.startapplab.stackexchange.data.repository

import com.startapplab.stackexchange.data.api.BadgeCountsDto
import com.startapplab.stackexchange.data.api.StackExchangeApi
import com.startapplab.stackexchange.data.api.UserDto
import com.startapplab.stackexchange.data.api.UsersResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    
    private lateinit var api: StackExchangeApi
    private lateinit var repository: UserRepository
    
    @Before
    fun setUp() {
        api = mockk()
        repository = UserRepository(api)
    }
    
    @Test
    fun `getUsers returns list of users sorted alphabetically`() = runTest {
        // Given
        val mockUserDtos = listOf(
            createTestUserDto(userId = 1, displayName = "Alice", reputation = 1000, location = "New York", creationDate = 1609459200L),
            createTestUserDto(userId = 2, displayName = "Bob", reputation = 500, location = "London", creationDate = 1612137600L)
        )
        val mockResponse = UsersResponse(
            items = mockUserDtos,
            hasMore = false,
            quotaMax = 300,
            quotaRemaining = 299
        )
        
        coEvery { 
            api.getUsers(
                page = 1, 
                pageSize = 20, 
                order = "asc", 
                sort = "name", 
                site = "stackoverflow"
            ) 
        } returns mockResponse
        
        // When
        val result = repository.getUsers(page = 1, pageSize = 20)
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(2, users.size)
        assertEquals("Alice", users[0].username)
        assertEquals("Bob", users[1].username)
        assertEquals(1000, users[0].reputation)
        assertEquals("New York", users[0].location)
    }
    
    @Test
    fun `getUsers returns up to 20 users`() = runTest {
        // Given
        val mockUserDtos = (1..20).map { index ->
            createTestUserDto(userId = index, displayName = "User$index", reputation = index * 100)
        }
        val mockResponse = UsersResponse(
            items = mockUserDtos,
            hasMore = true,
            quotaMax = 300,
            quotaRemaining = 299
        )
        
        coEvery { 
            api.getUsers(
                page = 1, 
                pageSize = 20, 
                order = "asc", 
                sort = "name", 
                site = "stackoverflow"
            ) 
        } returns mockResponse
        
        // When
        val result = repository.getUsers(page = 1, pageSize = 20)
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(20, users.size)
    }
    
    @Test
    fun `getUsers returns failure on API error`() = runTest {
        // Given
        coEvery { 
            api.getUsers(any(), any(), any(), any(), any()) 
        } throws RuntimeException("Network error")
        
        // When
        val result = repository.getUsers()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `searchUsers returns filtered users by name`() = runTest {
        // Given
        val mockUserDtos = listOf(
            createTestUserDto(userId = 1, displayName = "John Doe", reputation = 1500, location = "San Francisco", creationDate = 1609459200L),
            createTestUserDto(userId = 2, displayName = "Johnny Appleseed", reputation = 2000, location = "Seattle", creationDate = 1612137600L)
        )
        val mockResponse = UsersResponse(
            items = mockUserDtos,
            hasMore = false,
            quotaMax = 300,
            quotaRemaining = 298
        )
        
        coEvery { 
            api.searchUsers(
                inname = "John",
                page = 1, 
                pageSize = 20, 
                order = "asc", 
                sort = "name", 
                site = "stackoverflow"
            ) 
        } returns mockResponse
        
        // When
        val result = repository.searchUsers(query = "John", page = 1, pageSize = 20)
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(2, users.size)
        assertTrue(users.all { it.username.contains("John", ignoreCase = true) })
    }
    
    @Test
    fun `searchUsers with blank query calls getUsers instead`() = runTest {
        // Given
        val mockUserDtos = listOf(
            createTestUserDto(userId = 1, displayName = "Alice", reputation = 1000)
        )
        val mockResponse = UsersResponse(
            items = mockUserDtos,
            hasMore = false,
            quotaMax = 300,
            quotaRemaining = 299
        )
        
        coEvery { 
            api.getUsers(
                page = 1, 
                pageSize = 20, 
                order = "asc", 
                sort = "name", 
                site = "stackoverflow"
            ) 
        } returns mockResponse
        
        // When
        val result = repository.searchUsers(query = "", page = 1, pageSize = 20)
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(1, users.size)
        assertEquals("Alice", users[0].username)
    }
    
    @Test
    fun `searchUsers returns failure on API error`() = runTest {
        // Given
        coEvery { 
            api.searchUsers(any(), any(), any(), any(), any(), any()) 
        } throws RuntimeException("Search failed")
        
        // When
        val result = repository.searchUsers(query = "test")
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Search failed", result.exceptionOrNull()?.message)
    }
    
    private fun createTestUserDto(
        userId: Int,
        displayName: String,
        reputation: Int,
        profileImage: String? = null,
        location: String? = null,
        creationDate: Long? = null
    ) = UserDto(
        userId = userId,
        displayName = displayName,
        reputation = reputation,
        profileImage = profileImage,
        location = location,
        creationDate = creationDate,
        lastAccessDate = null,
        websiteUrl = null,
        badgeCounts = null,
        reputationChangeYear = null,
        reputationChangeQuarter = null,
        reputationChangeMonth = null,
        reputationChangeWeek = null,
        reputationChangeDay = null
    )
}
