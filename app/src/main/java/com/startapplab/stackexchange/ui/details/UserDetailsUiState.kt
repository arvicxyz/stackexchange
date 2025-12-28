package com.startapplab.stackexchange.ui.details

import com.startapplab.stackexchange.domain.model.User

data class UserDetailsUiState(
    val user: User? = null,
    val isLoading: Boolean = false
)
