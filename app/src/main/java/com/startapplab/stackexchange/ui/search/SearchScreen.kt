package com.startapplab.stackexchange.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startapplab.stackexchange.ui.theme.StackExchangeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onUserClick: (userId: Int, username: String, reputation: Int, location: String?, creationDate: String?) -> Unit = { _, _, _, _, _ -> }
) {
    var query by remember { mutableStateOf("") }

    val users = remember {
        listOf(
            MockUser(1, "Username1", 123, "San Francisco, CA", "Jan 15, 2025"),
            MockUser(2, "Username2", 390, "New York, NY", "Mar 22, 2025"),
            MockUser(3, "Username3", 0, "London, UK", "Dec 1, 2025"),
            MockUser(4, "Username4", 275, "Davao, Philippines", "Jul 8, 2025")
        )
    }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Search Users") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Search Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search users...") },
                    singleLine = true
                )
                
                Button(
                    onClick = { /* TODO: Search function */ }
                ) {
                    Text("SEARCH")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(users, key = { it.id }) { user ->
                    UserListItem(
                        reputation = user.reputation,
                        username = user.username,
                        onClick = { onUserClick(user.id, user.username, user.reputation, user.location, user.creationDate) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
private fun UserListItem(
    reputation: Int,
    username: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = reputation.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(60.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = username,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private data class MockUser(
    val id: Int,
    val username: String,
    val reputation: Int,
    val location: String?,
    val creationDate: String?
)
