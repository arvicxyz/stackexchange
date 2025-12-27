package com.startapplab.stackexchange.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.startapplab.stackexchange.ui.theme.StackExchangeTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Search Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    StackExchangeTheme {
        SearchScreen()
    }
}
