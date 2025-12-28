package com.startapplab.stackexchange.ui.search

import androidx.lifecycle.ViewModel
import com.startapplab.stackexchange.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    // Mock data
    private val mockUsers = listOf(
        User(1, "Username1", 123, "San Francisco, CA", "Jan 15, 2025"),
        User(2, "Username2", 390, "New York, NY", "Mar 22, 2025"),
        User(3, "Username3", 0, "London, UK", "Dec 1, 2025"),
        User(4, "Username4", 275, "Davao, Philippines", "Jul 8, 2025")
    )
    
    init {
        // Show all users initially
        _uiState.update { it.copy(users = mockUsers) }
    }
    
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }
    
    fun onSearch() {
        val query = _uiState.value.query
        if (query.isBlank()) {
            _uiState.update { it.copy(users = mockUsers) }
        } else {
            val filteredUsers = mockUsers.filter { 
                it.username.contains(query, ignoreCase = true) 
            }
            _uiState.update { it.copy(users = filteredUsers) }
        }
    }
}
