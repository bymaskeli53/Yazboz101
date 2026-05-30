package com.gundogar.yazboz101.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.ui.screens.yazboz.YazbozScreen
import com.gundogar.yazboz101.ui.screens.menu.MenuScreen
import com.gundogar.yazboz101.ui.screens.previous.PreviousGamesScreen
import com.gundogar.yazboz101.ui.screens.winner.WinnerScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.MenuScreen::class.qualifiedName!!,
        modifier = Modifier.padding(innerPadding),
        ) {

            composable<Screen.MenuScreen> {
            MenuScreen(onNavigateToYazboz = { playerNames, gameMode ->
                navController.navigate(Screen.YazbozScreen(players = playerNames.map {
                    Player(
                        it.name,
                        it.scores
                    )
                }, gameMode = gameMode))
            }, onNavigateToPreviousGames = {
                navController.navigate(Screen.PreviousGamesScreen)

            })
        }

        composable<Screen.YazbozScreen>(
            typeMap = mapOf(typeOf<Player>() to NavigationHelpers.parcelableType<Player>(),
                typeOf<List<Player>>() to NavigationHelpers.parcelableListType<Player>(),
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.YazbozScreen>()
            YazbozScreen(
                players = route.players,
                gameMode = route.gameMode,
                gameId = route.gameId,
                navController = navController
            )
        }

        composable<Screen.WinnerScreen>(
            typeMap = mapOf(
                typeOf<Player>() to NavigationHelpers.parcelableType<Player>(),
                typeOf<List<Player>>() to NavigationHelpers.parcelableListType<Player>()
            )
        ) { backStackEntry ->
            val players = backStackEntry.toRoute<Screen.WinnerScreen>().players
            WinnerScreen(players = players, navController = navController)
        }

        composable<Screen.PreviousGamesScreen>{
            PreviousGamesScreen(onResumeGame = { game ->
                navController.navigate(
                    Screen.YazbozScreen(
                        players = game.players,
                        gameMode = game.gameMode,
                        gameId = game.id
                    )
                )
            })
        }
    }
}