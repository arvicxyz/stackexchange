package com.startapplab.stackexchange.ui.search

import com.startapplab.stackexchange.domain.model.User

data class SearchUiState(
    val query: String = "",
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
