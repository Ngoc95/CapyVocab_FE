package com.example.capyvocab_fe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.navigation.graph.adminNavGraph
import com.example.capyvocab_fe.navigation.graph.authNavGraph
import com.example.capyvocab_fe.navigation.graph.userNavGraph

@Composable
fun AppNavGraph(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        authNavGraph(navController)

        adminNavGraph(navController)

        userNavGraph(navController)

    }
}