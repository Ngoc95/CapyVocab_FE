package com.example.capyvocab_fe.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.capyvocab_fe.navigation.Route

fun NavGraphBuilder.userNavGraph(navController: NavHostController) {
    navigation(
        route = Route.UserNavigation.route,
        startDestination = Route.ProfileScreen.route
    ) {
        //profile screen
        composable(route = Route.ProfileScreen.route) {
            //TODO: navigate to profile screen
        }
    }
}