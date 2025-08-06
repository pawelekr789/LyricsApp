package com.example.applyricspr.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applyricspr.R
import com.example.applyricspr.data.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import java.io.File


@Composable
fun SearchScreen() {
    // Zmienne stanu przechowujące dane z formularza i statusy
    var artist by remember { mutableStateOf("") }         // Wykonawca wpisany przez użytkownika
    var title by remember { mutableStateOf("") }          // Tytuł piosenki wpisany przez użytkownika
    var lyrics by remember { mutableStateOf("") }         // Pobranie tekstu piosenki
    var isLoading by remember { mutableStateOf(false) }   // Flaga ładowania (pokazuje loader)
    var errorMessage by remember { mutableStateOf<String?>(null) }  // Ewentualny komunikat błędu

    val coroutineScope = rememberCoroutineScope()         // Scope do uruchamiania asynchronicznych zadań
    val scrollState = rememberScrollState()               // Stan przewijania dla tekstu piosenki

    val context = LocalContext.current                     // Kontekst Androida (do obsługi plików)
    var saveToFavorites by remember { mutableStateOf(false) }  // Checkbox, czy zapisać do ulubionych

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)  // Odstępy między elementami w kolumnie
    ) {
        Text(
            text = stringResource(id = R.string.screen_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.author),
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text(stringResource(R.string.artist_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Przycisk uruchamiający wyszukiwanie tekstu piosenki
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true       // Włącz loader
                    errorMessage = null    // Reset błędu
                    lyrics = ""            // Reset tekstu piosenki
                    try {
                        // Wywołanie API (Retrofit) do wyszukania tekstu piosenki
                        val response = RetrofitInstance.api.searchLyrics(
                            trackName = title,
                            artistName = artist,
                            albumName = null,
                            duration = null
                        )
                        if (response.isSuccessful) {
                            val result = response.body()
                            // Pobierz tekst piosenki lub komunikat o braku
                            lyrics = result?.firstOrNull()?.plainLyrics ?: "Lyrics not found."

                            // Jeśli checkbox "Zapisz do ulubionych" jest zaznaczony, dopisz do pliku ulubionych
                            if (saveToFavorites) {
                                val favLine = "$artist - $title\n"
                                val favFile = File(context.filesDir, "favorites-saved.txt")
                                favFile.appendText(favLine)
                            }

                            // Dopisz wykonaną wyszukiwarkę do pliku historii wyszukiwań
                            val historyLine = "$artist - $title\n"
                            val historyFile = File(context.filesDir, "user_history.txt")
                            historyFile.appendText(historyLine)
                        } else {
                            // Obsługa błędu odpowiedzi HTTP
                            errorMessage = "Error: ${response.code()}"
                        }
                    } catch (e: Exception) {
                        // Obsługa wyjątków (np. brak sieci)
                        errorMessage = "Exception: ${e.localizedMessage}"
                    } finally {
                        isLoading = false  // Wyłącz loader niezależnie od wyniku
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = artist.isNotBlank() && title.isNotBlank()  // Przycisk aktywny tylko jeśli pola nie są puste
        ) {
            Text(stringResource(R.string.search_button))
        }

        // Checkbox do wyboru czy zapisać piosenkę do ulubionych
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = saveToFavorites,
                onCheckedChange = { saveToFavorites = it }
            )
            Text(stringResource(R.string.save_to_fav))
        }

        // Pokazuje animację ładowania jeśli trwa pobieranie tekstu
        if (isLoading) {
            CircularProgressIndicator()
        }

        // Pokazuje komunikat błędu jeśli wystąpił
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Wyświetla tekst piosenki, jeśli jest pobrany, z możliwością przewijania
        if (lyrics.isNotBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = lyrics,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}