package com.uiel.dopaminedetox

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DopamineDetoxApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(
                modifier = modifier,
                navController = navController
            )
        }
        composable("select") {
            SelectScreen(
                modifier = modifier,
                navController = navController,
            )
        }
    }
}
