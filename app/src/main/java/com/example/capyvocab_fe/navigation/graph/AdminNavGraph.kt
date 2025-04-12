package com.example.capyvocab_fe.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.capyvocab_fe.navigation.Route

fun NavGraphBuilder.adminNavGraph(navController: NavHostController) {
    navigation(
        route = Route.AdminNavigation.route,
        startDestination = Route.HomeScreen.route
    ) {
        //home screen
        composable(route = Route.HomeScreen.route) {
            //TODO: navigate to home screen
        }
        //courses screen
        composable(route = Route.CoursesScreen.route) {
            //TODO: navigate to courses screen
        }
        //user screen
        composable(route = Route.UsersScreen.route) {
            //TODO: navigate to user screen
        }
        //setting screen
        composable(route = Route.SettingScreen.route) {
            //TODO: navigate to setting screen
        }
    }
}