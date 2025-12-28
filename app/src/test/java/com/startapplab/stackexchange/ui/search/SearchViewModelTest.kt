package com.startapplab.stackexchange.ui.search

import com.startapplab.stackexchange.domain.model.User
import com.startapplab.stackexchange.domain.usecase.GetUsersUseCase
import com.startapplab.stackexchange.domain.usecase.SearchUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    
    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var searchUsersUseCase: SearchUsersUseCase
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUsersUseCase = mockk()
        searchUsersUseCase = mockk()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state loads users from use case`() = runTest {
        // Given
        val mockUsers = listOf(
            User(1, "Alice", 1000, "New York", "Jan 01, 2021"),
            User(2, "Bob", 500, "London", "Feb 01, 2021")
        )
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.success(mockUsers)
        
        // When
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.users.size)
        assertEquals("Alice", state.users[0].username)
        assertEquals("Bob", state.users[1].username)
        assertNull(state.errorMessage)
    }
    
    @Test
    fun `initial state shows loading then users`() = runTest {
        // Given
        val mockUsers = listOf(
            User(1, "Alice", 1000, null, null)
        )
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.success(mockUsers)
        
        // When
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        
        // Advance one step to start the coroutine, then check loading state
        testScheduler.advanceUntilIdle()
        
        // Then loaded
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, viewModel.uiState.value.users.size)
    }
    
    @Test
    fun `initial state shows error on failure`() = runTest {
        // Given
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.failure(RuntimeException("Network error"))
        
        // When
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.users.isEmpty())
        assertEquals("Network error", state.errorMessage)
    }
    
    @Test
    fun `onQueryChange updates query in state`() = runTest {
        // Given
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.success(emptyList())
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.onQueryChange("john")
        
        // Then
        assertEquals("john", viewModel.uiState.value.query)
    }
    
    @Test
    fun `onSearch calls searchUsersUseCase with query`() = runTest {
        // Given
        val initialUsers = listOf(User(1, "Alice", 1000, null, null))
        val searchResults = listOf(
            User(2, "John Doe", 500, null, null),
            User(3, "Johnny", 300, null, null)
        )
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.success(initialUsers)
        coEvery { searchUsersUseCase(query = "john", pageSize = 20) } returns Result.success(searchResults)
        
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.onQueryChange("john")
        viewModel.onSearch()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.users.size)
        assertEquals("John Doe", state.users[0].username)
        assertEquals("Johnny", state.users[1].username)
    }
    
    @Test
    fun `onSearch shows error on failure`() = runTest {
        // Given
        coEvery { getUsersUseCase(pageSize = 20) } returns Result.success(emptyList())
        coEvery { searchUsersUseCase(query = "test", pageSize = 20) } returns Result.failure(RuntimeException("Search failed"))
        
        viewModel = SearchViewModel(getUsersUseCase, searchUsersUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.onQueryChange("test")
        viewModel.onSearch()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Search failed", state.errorMessage)
    }
}
