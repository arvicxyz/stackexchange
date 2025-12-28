package com.startapplab.stackexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.startapplab.stackexchange.ui.details.UserDetailsScreen
import com.startapplab.stackexchange.ui.search.SearchScreen
import com.startapplab.stackexchange.ui.theme.StackExchangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StackExchangeTheme {
                StackExchangeApp()
            }
        }
    }
}

data class SelectedUser(
    val id: Int,
    val username: String,
    val reputation: Int,
    val location: String? = null,
    val creationDate: String? = null,
    val avatarUrl: String? = null
)

@Composable
fun StackExchangeApp() {
    var selectedUser by remember { mutableStateOf<SelectedUser?>(null) }
    
    if (selectedUser != null) {
        UserDetailsScreen(
            userId = selectedUser!!.id,
            username = selectedUser!!.username,
            reputation = selectedUser!!.reputation,
            location = selectedUser!!.location,
            creationDate = selectedUser!!.creationDate,
            avatarUrl = selectedUser!!.avatarUrl,
            onNavigateBack = { selectedUser = null }
        )
    } else {
        SearchScreen(
            onUserClick = { id, username, reputation, location, creationDate ->
                selectedUser = SelectedUser(
                    id = id,
                    username = username,
                    reputation = reputation,
                    location = location,
                    creationDate = creationDate
                )
            }
        )
    }
}
