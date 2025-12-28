package com.startapplab.stackexchange.domain.usecase

import com.startapplab.stackexchange.data.repository.UserRepository
import com.startapplab.stackexchange.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchUsersUseCaseTest {
    
    private lateinit var userRepository: UserRepository
    private lateinit var searchUsersUseCase: SearchUsersUseCase
    
    @Before
    fun setUp() {
        userRepository = mockk()
        searchUsersUseCase = SearchUsersUseCase(userRepository)
    }
    
    @Test
    fun `invoke calls repository with query`() = runTest {
        // Given
        val mockUsers = listOf(
            User(id = 1, username = "John Doe", reputation = 1000, profileImage = null, location = null, creationDate = null),
            User(id = 2, username = "Johnny", reputation = 500, profileImage = null, location = null, creationDate = null)
        )
        coEvery { 
            userRepository.searchUsers(query = "John", page = 1, pageSize = 20) 
        } returns Result.success(mockUsers)
        
        // When
        val result = searchUsersUseCase(query = "John", page = 1, pageSize = 20)
        
        // Then
        coVerify { userRepository.searchUsers(query = "John", page = 1, pageSize = 20) }
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }
    
    @Test
    fun `invoke uses default page and pageSize when not specified`() = runTest {
        // Given
        coEvery { 
            userRepository.searchUsers(query = "test", page = 1, pageSize = 20) 
        } returns Result.success(emptyList())
        
        // When
        searchUsersUseCase(query = "test")
        
        // Then
        coVerify { userRepository.searchUsers(query = "test", page = 1, pageSize = 20) }
    }
    
    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        coEvery { 
            userRepository.searchUsers(any(), any(), any()) 
        } returns Result.failure(RuntimeException("Search failed"))
        
        // When
        val result = searchUsersUseCase(query = "test")
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Search failed", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `invoke returns users matching search query`() = runTest {
        // Given
        val mockUsers = listOf(
            User(id = 1, username = "Alice Anderson", reputation = 1000, profileImage = null, location = "New York", creationDate = null),
            User(id = 2, username = "Alex Smith", reputation = 800, profileImage = null, location = "London", creationDate = null)
        )
        coEvery { 
            userRepository.searchUsers(query = "Al", page = 1, pageSize = 20) 
        } returns Result.success(mockUsers)
        
        // When
        val result = searchUsersUseCase(query = "Al")
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(2, users.size)
        assertTrue(users.all { it.username.startsWith("Al") })
    }
}
