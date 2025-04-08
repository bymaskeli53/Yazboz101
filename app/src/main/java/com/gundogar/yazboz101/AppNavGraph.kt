package com.gundogar.yazboz101

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController,innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Screen.MenuScreen,modifier = Modifier.padding(innerPadding)) {

        composable<Screen.MenuScreen> {
            MenuScreen(onNavigateToYazboz = {
                navController.navigate(Screen.YazbozScreen)
            })
        }

        composable<Screen.YazbozScreen> {
            YazbozScreen()
        }

    }
}