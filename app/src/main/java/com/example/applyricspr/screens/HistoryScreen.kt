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
fun HistoryScreen() {
    val context = LocalContext.current  // Pobieramy kontekst Androida, potrzebny do dostępu do plików
    var history by remember { mutableStateOf(listOf<String>()) }


    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "user_history.txt")
        if (file.exists()) {
            history = file.readLines()  // Wczytaj wszystkie linie pliku do listy
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()  // Kolumna zajmuje całą dostępną przestrzeń
            .padding(16.dp),  // Margines wokół treści
        verticalArrangement = Arrangement.spacedBy(8.dp)  // Odstęp 8 dp pomiędzy elementami
    ) {
        // Nagłówek ekranu
        Text(stringResource(R.string.search_his))

        // Jeśli lista historii jest pusta, pokaż komunikat
        if (history.isEmpty()) {
            Text(stringResource(R.string.no_search_his))
        } else {
            // W przeciwnym wypadku wyświetl każdą linię historii jako osobny tekst
            history.forEach { line ->
                Text(text = line)
            }
        }
    }
}