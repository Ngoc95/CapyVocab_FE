package com.example.capyvocab_fe.admin.navigator

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigation
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem
import com.example.capyvocab_fe.admin.user.presentation.users_screen.UserListEvent
import com.example.capyvocab_fe.admin.user.presentation.users_screen.UserListViewModel
import com.example.capyvocab_fe.admin.user.presentation.users_screen.UserScreen
import com.example.capyvocab_fe.navigation.Route

@Composable
fun AdminNavigator() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                icon = R.drawable.admin_home,
                selectedIcon = R.drawable.admin_selected_home,
                text = "Trang chủ"
            ),
            BottomNavigationItem(
                icon = R.drawable.admin_course,
                selectedIcon = R.drawable.admin_selected_course,
                text = "Khoá học"
            ),
            BottomNavigationItem(
                icon = R.drawable.admin_user,
                selectedIcon = R.drawable.admin_selected_user,
                text = "Người dùng"
             ),
            BottomNavigationItem(
                icon = R.drawable.ic_setting,
               selectedIcon = R.drawable.ic_selected_setting,
                text = "Cài đặt"
            )
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.CoursesScreen.route -> 1
        Route.UsersScreen.route -> 2
        Route.SettingScreen.route -> 3
        else -> 0

    }
    //hide navbar when in multi select, topic, word,...
    val isBottomVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.CoursesScreen.route ||
                backStackState?.destination?.route == Route.UsersScreen.route ||
                backStackState?.destination?.route == Route.SettingScreen.route

    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (isBottomVisible) {
            BottomNavigation(
                items = bottomNavigationItems,
                selected = selectedItem,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController = navController,
                            route = Route.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController = navController,
                            route = Route.CoursesScreen.route
                        )

                        2 -> navigateToTab(
                            navController = navController,
                            route = Route.UsersScreen.route
                        )

                        3 -> navigateToTab(
                            navController = navController,
                            route = Route.SettingScreen.route
                        )
                    }
                }
            )
        }
    }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
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

                val viewModel: UserListViewModel = hiltViewModel()
                val userListState by viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.onEvent(UserListEvent.LoadUsers)
                }

                UserScreen(
                    users = userListState.users,
                    errorMessage = userListState.errorMessage,
                    isLoading = userListState.isLoading,
                    isEndReached = userListState.isEndReached,
                    onUserExpandToggle = { user -> /* toggle logic nếu muốn */ },
                    onUserSave = { user, password, confirmPassword, avatarUri ->
                        viewModel.onEvent(
                            UserListEvent.SaveUser(user, password, confirmPassword, avatarUri)
                        )
                    },
                    onUserDelete = { userToDelete ->
                        // TODO: Gọi viewModel xoá user nếu có
                    },
                    onLoadMore = {
                        viewModel.onEvent(UserListEvent.LoadMoreUsers)
                    }
                )
            }
            //setting screen
            composable(route = Route.SettingScreen.route) {
                //TODO: navigate to setting screen
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