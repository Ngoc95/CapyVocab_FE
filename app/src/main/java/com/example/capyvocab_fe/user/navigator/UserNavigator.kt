package com.example.capyvocab_fe.user.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.community.presentation.CommunityEvent
import com.example.capyvocab_fe.user.community.presentation.CommunityScreen
import com.example.capyvocab_fe.user.community.presentation.CommunityViewModel
import com.example.capyvocab_fe.user.community.presentation.PostScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnEvent
import com.example.capyvocab_fe.user.learn.presentation.CourseScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnFlashcardScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnViewModel
import com.example.capyvocab_fe.user.learn.presentation.TopicsInCourseScreen
import com.example.capyvocab_fe.user.navigator.components.UserBottomNavigation
import com.example.capyvocab_fe.user.profile.presentation.ProfileScreen
import com.example.capyvocab_fe.user.profile.presentation.ProfileViewModel

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
                selectedIcon = R.drawable.user_selected_profile ,
                text = "Hồ sơ"
            ),
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    val learnViewModel: LearnViewModel = hiltViewModel()
    val learnState by learnViewModel.state.collectAsState()

    val communityViewModel: CommunityViewModel = hiltViewModel()
    val communityState by communityViewModel.state.collectAsState()

    val profileViewModel: ProfileViewModel = hiltViewModel()

    selectedItem = when(backStackState?.destination?.route) {
        Route.UserCommunityScreen.route -> 0
        Route.UserReviewScreen.route -> 1
        Route.UserLearnScreen.route -> 2
        Route.UserTestScreen.route -> 3
        Route.UserProfileScreen.route -> 4
        else -> 0
    }

    //hide navbar when in topic, word
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
                        selectedItem = index
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
                CommunityScreen(
                    viewModel = communityViewModel,
                    onPostComment = { post ->
                        //communityViewModel.onEvent(CommunityEvent.LoadComments(post.id, null))
                        navController.navigate("${Route.UserPostScreen.route}/${post.id}")
                    }
                )
            }
            //Post screen in community Screen
            composable(
                route = "${Route.UserPostScreen.route}/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId")

                LaunchedEffect(postId) {
                    communityViewModel.onEvent(CommunityEvent.ClearScreenPost);
                    communityViewModel.onEvent(CommunityEvent.GetPostById(postId!!.toInt()))
                }

                communityState.selectedPost?.let { post ->
                    PostScreen(
                        post = post,
                        viewModel = communityViewModel,
                        navController = navController,
                        onBackClick = { navController.popBackStack() },
                    )
                }


            }

            //user review screen
            composable(route = Route.UserReviewScreen.route) {
                //TODO: navigate to user learn screen
            }
            //user course screen
            composable(route = Route.UserLearnScreen.route) {
                CourseScreen(
                    viewModel = learnViewModel,
                    onCourseClick = { course ->
                        learnViewModel.onEvent(LearnEvent.ClearTopics)
                        navController.navigate("${Route.UserTopicsScreen.route}/${course.id}")
                    }
                )
            }


            // Topics in learn screen
            composable(
                route = "${Route.UserTopicsScreen.route}/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt("courseId")
                LaunchedEffect(courseId) {
                    learnViewModel.onEvent(LearnEvent.GetCourseById(courseId!!.toInt()))
                }
                learnState.selectedCourse?.let { course ->
                    TopicsInCourseScreen(
                        course = course,
                        onTopicClick = { topic ->
                            navController.navigate("${Route.UserWordsScreen.route}/${topic.id}")
                        },
                        viewModel = learnViewModel,
                        navController = navController,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // Words in topic
            composable(
                route = "${Route.UserWordsScreen.route}/{topicId}",
                arguments = listOf(navArgument("topicId") { type = NavType.IntType })
            ) { backStackEntry ->
                val topicId = backStackEntry.arguments?.getInt("topicId")

                LaunchedEffect(topicId) {
                    learnViewModel.onEvent(LearnEvent.GetTopicById(topicId!!))
                }

                learnState.selectedTopic?.let { topic ->
                    LearnFlashcardScreen(
                        topic = topic,
                        viewModel = learnViewModel,
                        onComplete = {
                            navController.popBackStack("${Route.UserWordsScreen.route}/${topic.id}", inclusive = true)
                        },
                        navController = navController
                    )
                }
            }

            //user test screen
            composable(route = Route.UserTestScreen.route) {
                //TODO: navigate to user test screen
            }
            //user profile screen
            composable(route = Route.UserProfileScreen.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                )
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