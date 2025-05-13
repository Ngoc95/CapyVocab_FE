package com.example.capyvocab_fe.admin.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.course.presentation.CourseEvent
import com.example.capyvocab_fe.admin.course.presentation.CourseListViewModel
import com.example.capyvocab_fe.admin.course.presentation.CourseScreen
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigation
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem
import com.example.capyvocab_fe.admin.topic.presentation.TopicEvent
import com.example.capyvocab_fe.admin.topic.presentation.TopicListViewModel
import com.example.capyvocab_fe.admin.topic.presentation.TopicScreen
import com.example.capyvocab_fe.admin.user.presentation.UserListViewModel
import com.example.capyvocab_fe.admin.user.presentation.UserScreen
import com.example.capyvocab_fe.admin.word.presentation.WordListViewModel
import com.example.capyvocab_fe.admin.word.presentation.WordScreen
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

    //get the viewModel to access multi select state
    val userViewModel: UserListViewModel = hiltViewModel()
    val userListState by userViewModel.state.collectAsState()

    val courseViewModel: CourseListViewModel = hiltViewModel()
    val courseListState by courseViewModel.state.collectAsState()

    val topicViewModel: TopicListViewModel = hiltViewModel()
    val topicListState by topicViewModel.state.collectAsState()

    val wordViewModel: WordListViewModel = hiltViewModel()
    val wordListState by wordViewModel.state.collectAsState()

    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.CoursesScreen.route -> 1
        Route.UsersScreen.route -> 2
        Route.SettingScreen.route -> 3
        else -> 0

    }
    //hide navbar when in multi select, topic, word,...
    val isBottomVisible = remember(
        backStackState,
        userListState.isMultiSelecting,
        courseListState.isMultiSelecting,
        topicListState.isMultiSelecting,
        wordListState.isMultiSelecting
    ) {
        //check if is on main screen
        val isOnMainScreen = backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.CoursesScreen.route ||
                backStackState?.destination?.route == Route.UsersScreen.route ||
                backStackState?.destination?.route == Route.SettingScreen.route
        //check if is multi select
        val isMultiSelecting =
            (backStackState?.destination?.route == Route.UsersScreen.route && userListState.isMultiSelecting) ||
                    (backStackState?.destination?.route == Route.CoursesScreen.route && courseListState.isMultiSelecting ||
                            backStackState?.destination?.route == Route.TopicsScreen.route && topicListState.isMultiSelecting ||
                            backStackState?.destination?.route == Route.WordsScreen.route && wordListState.isMultiSelecting)
        //visible if is on main screen and not in multi select
        isOnMainScreen && !isMultiSelecting
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (isBottomVisible) {
            BottomNavigation(
                items = bottomNavigationItems,
                selected = selectedItem,
                onItemClick = { index ->
                    if (index == selectedItem) return@BottomNavigation
                    selectedItem == index
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
                CourseScreen(
                    onCourseClick = { course ->
                        navController.navigate("${Route.TopicsScreen.route}/${course.id}")
                    },
                    viewModel = courseViewModel,
                    navController = navController
                )
            }
            //topics in course screen
            composable(
                route = "${Route.TopicsScreen.route}/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt("courseId")
                LaunchedEffect(courseId) {
                    courseViewModel.onEvent(CourseEvent.GetCourseById(courseId!!.toInt()))
                }
                courseListState.selectedCourse?.let { course ->
                    TopicScreen(
                        course = course,
                        onBackClick = { navController.popBackStack() },
                        onTopicClick = { topic ->
                            navController.navigate("${Route.WordsScreen.route}/${topic.id}")
                        },
                        viewModel = topicViewModel,
                        navController = navController
                    )
                }
            }
            //words in topic screen
            composable(
                route = "${Route.WordsScreen.route}/{topicId}",
                arguments = listOf(navArgument("topicId") { type = NavType.IntType })
            ) { backStackEntry ->
                val topicId = backStackEntry.arguments?.getInt("topicId")
                LaunchedEffect(topicId) {
                    topicViewModel.onEvent(TopicEvent.GetTopicById(topicId!!.toInt()))
                }
                topicListState.selectedTopic?.let { topic ->
                    WordScreen(
                        topic = topic,
                        onBackClick = { navController.popBackStack() },
                        viewModel = wordViewModel,
                        navController = navController
                    )
                }
            }
            //user screen
            composable(route = Route.UsersScreen.route) {
                UserScreen(
                    state = userListState,
                    navController = navController,
                    onEvent = userViewModel::onEvent
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