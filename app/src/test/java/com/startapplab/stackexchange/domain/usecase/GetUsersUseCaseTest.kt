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

class GetUsersUseCaseTest {
    
    private lateinit var userRepository: UserRepository
    private lateinit var getUsersUseCase: GetUsersUseCase
    
    @Before
    fun setUp() {
        userRepository = mockk()
        getUsersUseCase = GetUsersUseCase(userRepository)
    }
    
    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        // Given
        val mockUsers = listOf(
            User(1, "Alice", 1000, "New York", "Jan 01, 2021"),
            User(2, "Bob", 500, "London", "Feb 01, 2021")
        )
        coEvery { userRepository.getUsers(page = 1, pageSize = 20) } returns Result.success(mockUsers)
        
        // When
        val result = getUsersUseCase(page = 1, pageSize = 20)
        
        // Then
        coVerify { userRepository.getUsers(page = 1, pageSize = 20) }
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }
    
    @Test
    fun `invoke uses default parameters when not specified`() = runTest {
        // Given
        coEvery { userRepository.getUsers(page = 1, pageSize = 20) } returns Result.success(emptyList())
        
        // When
        getUsersUseCase()
        
        // Then
        coVerify { userRepository.getUsers(page = 1, pageSize = 20) }
    }
    
    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        coEvery { userRepository.getUsers(any(), any()) } returns Result.failure(RuntimeException("Network error"))
        
        // When
        val result = getUsersUseCase()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `invoke returns up to 20 users alphabetically`() = runTest {
        // Given
        val mockUsers = (1..20).map { index ->
            User(index, "User${('A'.code + index - 1).toChar()}", index * 100, null, null)
        }
        coEvery { userRepository.getUsers(page = 1, pageSize = 20) } returns Result.success(mockUsers)
        
        // When
        val result = getUsersUseCase(page = 1, pageSize = 20)
        
        // Then
        assertTrue(result.isSuccess)
        val users = result.getOrNull()!!
        assertEquals(20, users.size)
        assertEquals("UserA", users[0].username)
        assertEquals("UserT", users[19].username)
    }
}
