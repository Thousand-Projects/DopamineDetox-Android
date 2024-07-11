package com.uiel.dopaminedetox

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DetoxNavHost(
    modifier: Modifier = Modifier,
    packageName: String,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "breath",
    ) {
        composable("breath") {
            BreathScreen(
                modifier = modifier,
                navController = navController,
            )
        }
        composable("think") {
            ThinkScreen(
                modifier = modifier,
                navController = navController,
                packageName = packageName,
            )
        }
    }
}
