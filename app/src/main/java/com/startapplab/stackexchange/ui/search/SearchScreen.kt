package com.startapplab.stackexchange.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startapplab.stackexchange.domain.model.User
import com.startapplab.stackexchange.ui.search.components.UserListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onUserClick: (User) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    val isLoading by remember { derivedStateOf { uiState.isLoading } }
    val users by remember { derivedStateOf { uiState.users } }
    val errorMessage by remember { derivedStateOf { uiState.errorMessage } }
    val query by remember { derivedStateOf { uiState.query } }
    val hasNoUsersAndNoError by remember { derivedStateOf { users.isEmpty() && errorMessage == null } }
    
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                TopAppBar(
                    title = { Text("Users", fontWeight = FontWeight.Bold) },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    )
                )
                
                SearchBar(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = viewModel::onSearch,
                    enabled = !isLoading,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    LoadingContent()
                }
                hasNoUsersAndNoError -> {
                    EmptyContent()
                }
                else -> {
                    UserListContent(
                        users = users,
                        errorMessage = errorMessage,
                        onRetry = viewModel::onSearch,
                        onUserClick = onUserClick
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No users found",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UserListContent(
    users: List<User>,
    errorMessage: String?,
    onRetry: () -> Unit,
    onUserClick: (User) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Error Message
        errorMessage?.let { error ->
            item(key = "error") {
                ErrorCard(
                    error = error,
                    onRetry = onRetry
                )
            }
        }
        
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserListItem(
                reputation = user.reputation,
                username = user.username,
                profileImage = user.profileImage,
                onClick = { onUserClick(user) }
            )
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRetry) {
                Text(
                    text = "Retry",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val showClearButton by remember(query) { derivedStateOf { query.isNotEmpty() } }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    
    val textStyle = remember(onSurfaceColor) {
        TextStyle(
            color = onSurfaceColor,
            fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
        )
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(surfaceColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Input
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp),
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(
                color = onSurfaceColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ),
            cursorBrush = SolidColor(primaryColor),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search users...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = onSurfaceVariantColor
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Clear Button
        if (showClearButton) {
            IconButton(
                onClick = {
                    onQueryChange("")
                    onSearch()
                },
                enabled = enabled,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = onSurfaceVariantColor
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        // Search Button
        IconButton(
            onClick = onSearch,
            enabled = enabled,
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = primaryColor,
                contentColor = Color.White,
                disabledContainerColor = primaryColor.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}
