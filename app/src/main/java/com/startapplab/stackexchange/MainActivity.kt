package com.startapplab.stackexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.startapplab.stackexchange.ui.details.UserDetailsScreen
import com.startapplab.stackexchange.ui.search.SearchScreen
import com.startapplab.stackexchange.domain.model.User
import com.startapplab.stackexchange.ui.theme.StackExchangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            
            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    ),
                    navigationBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(0xFF1C1D1E.toInt())
                    } else {
                        SystemBarStyle.light(0xFFF1F2F3.toInt(), 0xFF1C1D1E.toInt())
                    }
                )
                onDispose {}
            }
            
            StackExchangeTheme {
                StackExchangeApp()
            }
        }
    }
}

@Composable
fun StackExchangeApp() {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    
    if (selectedUser != null) {
        UserDetailsScreen(
            user = selectedUser!!,
            onNavigateBack = { selectedUser = null }
        )
    } else {
        SearchScreen(
            onUserClick = { user ->
                selectedUser = user
            }
        )
    }
}
