package com.example.applyricspr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.applyricspr.screens.*

object Routes {
    const val SEARCH = "search"       // Ekran wyszukiwania
    const val FAVORITES = "favorites" // Ekran ulubionych
    const val HISTORY = "history"     // Ekran historii
}

// Komponent Composable odpowiedzialny za konfigurację nawigacji w aplikacji.
// Przyjmuje NavController i definiuje ścieżki do poszczególnych ekranów.
@Composable
fun AppNavigation(navController: NavHostController) {
    // Tworzy "kontener" nawigacyjny, który zaczyna od ekranu SEARCH.
    NavHost(navController = navController, startDestination = Routes.SEARCH) {

        // Rejestruje trasę "search", która wyświetla ekran wyszukiwania
        composable(Routes.SEARCH) { SearchScreen() }

        // Rejestruje trasę "favorites", która wyświetla ekran ulubionych
        composable(Routes.FAVORITES) { FavoritesScreen() }

        // Rejestruje trasę "history", która wyświetla ekran historii
        composable(Routes.HISTORY) { HistoryScreen() }
    }
}
