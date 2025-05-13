package com.example.capyvocab_fe.user.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.navigator.components.AdminBottomNavigation
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.navigator.components.UserBottomNavigation

@Composable
fun UserNavigator() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                icon = R.drawable.user_community,
                selectedIcon = R.drawable.user_selected_community,
                text = "Cộng đồng"
            ),
            BottomNavigationItem(
                icon = R.drawable.user_review,
                selectedIcon = R.drawable.user_selected_review,
                text = "Ôn tập"
            ),
            BottomNavigationItem(
                icon = R.drawable.user_learn,
                selectedIcon = R.drawable.user_selected_learn,
                text = "Học từ vựng"
            ),
            BottomNavigationItem(
                icon = R.drawable.user_test,
                selectedIcon = R.drawable.user_selected_test,
                text = "Kiểm tra"
            ),
            BottomNavigationItem(
                icon = R.drawable.user_profile,
                selectedIcon = R.drawable.user_selected_profile,
                text = "Hồ sơ"
            ),
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    selectedItem = when(backStackState?.destination?.route) {
        Route.UserCommunityScreen.route -> 0
        Route.UserReviewScreen.route -> 1
        Route.UserLearnScreen.route -> 2
        Route.UserTestScreen.route -> 3
        Route.UserProfileScreen.route -> 4
        else -> 0
    }

    val isBottomVisible = remember(backStackState) {
        backStackState?.destination?.route == Route.UserCommunityScreen.route ||
                backStackState?.destination?.route == Route.UserReviewScreen.route ||
                backStackState?.destination?.route == Route.UserLearnScreen.route ||
                backStackState?.destination?.route == Route.UserTestScreen.route ||
                backStackState?.destination?.route == Route.UserProfileScreen.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomVisible) {
                UserBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        if (index == selectedItem) return@UserBottomNavigation
                        selectedItem == index
                        when(index) {
                            0 -> navigateToTab(
                                navController = navController,
                                route = Route.UserCommunityScreen.route
                            )
                            1 -> navigateToTab(
                                navController = navController,
                                route = Route.UserReviewScreen.route
                            )
                            2 -> navigateToTab(
                                navController = navController,
                                route = Route.UserLearnScreen.route
                            )
                            3 -> navigateToTab(
                                navController = navController,
                                route = Route.UserTestScreen.route
                            )
                            4 -> navigateToTab(
                                navController = navController,
                                route = Route.UserProfileScreen.route
                            )
                        }
                    }
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.UserCommunityScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            //user community screen
            composable(route = Route.UserCommunityScreen.route) {
                //TODO: navigate to user home screen
            }
            //user review screen
            composable(route = Route.UserReviewScreen.route) {
                //TODO: navigate to user learn screen
            }
            //user learn screen
            composable(route = Route.UserLearnScreen.route) {
                //TODO: navigate to user community screen
            }
            //user test screen
            composable(route = Route.UserTestScreen.route) {
                //TODO: navigate to user test screen
            }
            //user profile screen
            composable(route = Route.UserProfileScreen.route) {
                //TODO: navigate to user profile screen
            }
        }
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}