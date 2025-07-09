package com.gundogar.yazboz101

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlin.reflect.typeOf

@Composable
fun AppNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.MenuScreen::class.qualifiedName!!,
        modifier = Modifier.padding(innerPadding),
        ) {

            composable<Screen.MenuScreen> {
            MenuScreen(onNavigateToYazboz = { p1, p2, p3, p4 ->
                // val oyuncularJson = Uri.encode(Json.encodeToString(oyuncular))
                // navController.navigate("${Screen.YazbozScreen::class.qualifiedName}?oyuncular=$oyuncularJson")
                navController.navigate(Screen.YazbozScreen(player = Player("Muhammet", listOf(0,0,0,0))))
            })
        }

        composable<Screen.YazbozScreen>(
            typeMap = mapOf(typeOf<Player>() to NavigationHelpers.parcelableType<Player>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.YazbozScreen>().player
            val name = args.name
            YazbozScreen(s1 = name,s2 = name, s3 = name, s4 = name)
        }
    }
}