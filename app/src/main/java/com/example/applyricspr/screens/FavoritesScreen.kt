package com.example.applyricspr.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.applyricspr.R
import java.io.File

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    var favorites by remember { mutableStateOf(listOf<String>()) }

    val file = File(context.filesDir, "favorites-saved.txt")

    fun loadFavorites() {
        if (file.exists()) {
            favorites = file.readLines()
        } else {
            favorites = emptyList()
        }
    }

    LaunchedEffect(Unit) {
        loadFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.saved_fav))

        if (favorites.isEmpty()) {
            Text(stringResource(R.string.no_saved_fav))
        } else {
            favorites.forEach { line ->
                Text(text = line)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Czyści plik
            if (file.exists()) {
                file.writeText("")
            }
            // Odśwież listę ulubionych
            loadFavorites()
        }) {
            Text(stringResource(R.string.clear_fav))
        }
    }
}
