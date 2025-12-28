package com.startapplab.stackexchange.ui.details

import androidx.lifecycle.ViewModel
import com.startapplab.stackexchange.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserDetailsUiState())
    val uiState: StateFlow<UserDetailsUiState> = _uiState.asStateFlow()
    
    fun setUser(user: User) {
        _uiState.value = UserDetailsUiState(user = user)
    }
}
