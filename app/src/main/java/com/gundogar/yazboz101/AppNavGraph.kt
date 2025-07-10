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
            MenuScreen(onNavigateToYazboz = { isimler ->
                // val oyuncularJson = Uri.encode(Json.encodeToString(oyuncular))
                // navController.navigate("${Screen.YazbozScreen::class.qualifiedName}?oyuncular=$oyuncularJson")
                navController.navigate(Screen.YazbozScreen(players = isimler.map { Player(it.name, it.scores) }))
            },onNavigateToPreviousGames = {
                navController.navigate(Screen.PreviousGamesScreen)

            })
        }

        composable<Screen.YazbozScreen>(
            typeMap = mapOf(typeOf<Player>() to NavigationHelpers.parcelableType<Player>(),
                typeOf<List<Player>>() to NavigationHelpers.parcelableListType<Player>(),
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.YazbozScreen>().players
            YazbozScreen(players = args, navController = navController)
        }

        composable<Screen.PreviousGamesScreen>{
            PreviousGamesScreen()

        }
    }
}