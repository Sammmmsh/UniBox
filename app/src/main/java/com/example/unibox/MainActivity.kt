package com.example.unibox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.unibox.presentation.detail.DetailScreen
import com.example.unibox.presentation.main.MainScreen
import com.example.unibox.presentation.settings.SettingsScreen
import com.example.unibox.presentation.theme.UniBoxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.isSystemInDarkTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferences: com.example.unibox.domain.repository.ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themePreferences.themeMode.collectAsState(
                initial = com.example.unibox.domain.model.ThemeMode.SYSTEM
            )
            
            val isDarkTheme = when (themeMode) {
                com.example.unibox.domain.model.ThemeMode.LIGHT -> false
                com.example.unibox.domain.model.ThemeMode.DARK -> true
                com.example.unibox.domain.model.ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            UniBoxTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = "main",
                            enterTransition = { fadeIn(tween(180)) },
                            exitTransition = { fadeOut(tween(140)) }
                        ) {
                            MainScreen(
                                onItemClick = { item ->
                                    navController.navigate("detail/${item.id}")
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                }
                            )
                        }

                        composable(
                            route = "detail/{itemId}",
                            arguments = listOf(
                                navArgument("itemId") { type = NavType.LongType }
                            ),
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Start,
                                    tween(220)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.End,
                                    tween(180)
                                )
                            }
                        ) {
                            DetailScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // Settings screen (UX fix #10)
                        composable(
                            route = "settings",
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Start,
                                    tween(220)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.End,
                                    tween(180)
                                )
                            }
                        ) {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
