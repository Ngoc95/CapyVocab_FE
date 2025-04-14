package com.example.capyvocab_fe.navigation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginScreen
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginViewModel
import com.example.capyvocab_fe.auth.presentation.register_screen.RegisterScreen
import com.example.capyvocab_fe.auth.presentation.register_screen.RegisterViewModel
import com.example.capyvocab_fe.navigation.Route

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Route.AuthNavigation.route,
        startDestination = Route.LoginScreen.route
    ) {
        //login screen
        composable(route = Route.LoginScreen.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        //register screen
        composable(route = Route.RegisterScreen.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

    }
}