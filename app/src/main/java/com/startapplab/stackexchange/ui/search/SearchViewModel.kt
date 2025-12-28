package com.startapplab.stackexchange.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startapplab.stackexchange.domain.usecase.GetUsersUseCase
import com.startapplab.stackexchange.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialUsers()
    }
    
    private fun loadInitialUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getUsersUseCase(pageSize = 20)
                .onSuccess { users ->
                    _uiState.update { 
                        it.copy(users = users, isLoading = false, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }
    
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }
    
    fun onSearch() {
        val query = _uiState.value.query
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            searchUsersUseCase(query = query, pageSize = 20)
                .onSuccess { users ->
                    _uiState.update { 
                        it.copy(users = users, isLoading = false, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }
}
