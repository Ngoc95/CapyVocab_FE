package com.example.capyvocab_fe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.capyvocab_fe.admin.navigator.AdminNavigator
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginScreen
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginViewModel
import com.example.capyvocab_fe.user.navigator.UserNavigator

@Composable
fun AppNavGraph(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        //auth
        navigation(
            route = Route.AuthNavigation.route,
            startDestination = Route.LoginScreen.route
        ) {
            //login screen
            composable(route = Route.LoginScreen.route) {
                val viewModel: LoginViewModel = hiltViewModel()
                LoginScreen(viewModel)
            }

            //register screen
            composable(route = Route.RegisterScreen.route) {
                //TODO: navigate to register screen
            }

        }
        //admin
        navigation(
            route = Route.AdminNavigation.route,
            startDestination = Route.AdminNavigator.route
        ) {
            composable(route = Route.AdminNavigator.route) {
                AdminNavigator()
            }
        }

        navigation(
            route = Route.UserNavigation.route,
            startDestination = Route.UserNavigator.route
        ) {
            composable(route = Route.UserNavigator.route) {
                UserNavigator()
            }
        }
    }
}