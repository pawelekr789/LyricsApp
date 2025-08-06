package com.example.applyricspr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
// import androidx.compose.material.icons.filled.History  // (nieużywany, alternatywna ikona mogłaby być użyta)
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.applyricspr.navigation.AppNavigation
import com.example.applyricspr.navigation.Routes
import com.example.applyricspr.ui.theme.ApplyricsPRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Ustawienie motywu aplikacji
            ApplyricsPRTheme {
                val navController = rememberNavController() // Kontroler nawigacji
                val navBackStackEntry by navController.currentBackStackEntryAsState() // Obserwacja bieżącego ekranu
                val currentDestination = navBackStackEntry?.destination // Bieżący cel nawigacji

                // Lista zakładek dolnego menu (ścieżki do ekranów)
                val bottomNavItems = listOf(
                    Routes.SEARCH,
                    Routes.FAVORITES,
                    Routes.HISTORY
                )

                // Układ z dolnym paskiem nawigacji
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            // Tworzenie przycisków nawigacji dla każdego ekranu
                            bottomNavItems.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any { it.route == screen } == true,
                                    onClick = {
                                        // Przejście do nowego ekranu bez duplikowania instancji
                                        navController.navigate(screen) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true // Zapisz stan poprzedniego ekranu
                                            }
                                            launchSingleTop = true // Nie twórz duplikatów ekranu
                                            restoreState = true // Przywróć stan, jeśli to możliwe
                                        }
                                    },
                                    label = {
                                        // Etykieta przycisku dolnego menu
                                        Text(
                                            when (screen) {
                                                Routes.SEARCH -> "Search"
                                                Routes.FAVORITES -> "Favorites"
                                                Routes.HISTORY -> "History"
                                                else -> ""
                                            }
                                        )
                                    },
                                    icon = {
                                        // Ikona przycisku w dolnym menu
                                        Icon(
                                            when (screen) {
                                                Routes.SEARCH -> Icons.Default.Search
                                                Routes.FAVORITES -> Icons.Default.Favorite
                                                Routes.HISTORY -> Icons.Default.Menu // Można zamienić na Icons.Default.History
                                                else -> Icons.Default.Menu
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { padding ->
                    // Powierzchnia główna zawierająca ekran z nawigacją
                    Surface(
                        modifier = Modifier
                            .padding(padding) // Uwzględnienie miejsca na pasek nawigacji
                            .fillMaxSize()
                    ) {
                        // Uruchomienie logiki nawigacyjnej aplikacji
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}
