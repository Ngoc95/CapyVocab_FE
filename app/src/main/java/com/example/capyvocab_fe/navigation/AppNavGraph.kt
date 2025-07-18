package com.example.capyvocab_fe.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.capyvocab_fe.admin.navigator.AdminNavigator
import com.example.capyvocab_fe.auth.presentation.forgot_password_screen.ForgotPasswordCodeScreen
import com.example.capyvocab_fe.auth.presentation.forgot_password_screen.ForgotPasswordCodeViewModel
import com.example.capyvocab_fe.auth.presentation.forgot_password_screen.ForgotPasswordEmailScreen
import com.example.capyvocab_fe.auth.presentation.forgot_password_screen.ForgotPasswordEmailViewModel
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginScreen
import com.example.capyvocab_fe.auth.presentation.login_screen.LoginViewModel
import com.example.capyvocab_fe.auth.presentation.otp_screen.OtpScreen
import com.example.capyvocab_fe.auth.presentation.otp_screen.OtpViewModel
import com.example.capyvocab_fe.auth.presentation.register_screen.RegisterScreen
import com.example.capyvocab_fe.auth.presentation.register_screen.RegisterViewModel
import com.example.capyvocab_fe.user.navigator.UserNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        //auth
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
            //otp screen
            composable(route = Route.OtpScreen.route) {
                val viewModel: OtpViewModel = hiltViewModel()
                OtpScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            // Forgot password email
            composable(route = Route.ForgotPasswordEmailScreen.route) {
                val viewModel: ForgotPasswordEmailViewModel = hiltViewModel()
                ForgotPasswordEmailScreen(viewModel = viewModel, navController = navController)
            }

            // Forgot password code + New password
            composable(
                route = Route.ForgotPasswordCodeScreen.route + "?email={email}",
                arguments = listOf(navArgument("email") {
                    type = NavType.StringType
                })
            ) {
                val email = it.arguments?.getString("email") ?: ""
                val viewModel: ForgotPasswordCodeViewModel = hiltViewModel()
                ForgotPasswordCodeScreen(email = email, viewModel = viewModel, navController = navController)
            }
        }
        //admin
        navigation(
            route = Route.AdminNavigation.route,
            startDestination = Route.AdminNavigator.route
        ) {
            composable(route = Route.AdminNavigator.route) {
                AdminNavigator(rootNavController = navController)
            }
        }
        //user
        navigation(
            route = Route.UserNavigation.route,
            startDestination = Route.UserNavigator.route
        ) {
            composable(route = Route.UserNavigator.route) {
                UserNavigator(rootNavController = navController)
            }
        }
    }
}