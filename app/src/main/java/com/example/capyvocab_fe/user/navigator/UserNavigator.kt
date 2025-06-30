package com.example.capyvocab_fe.user.navigator

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.capyvocab_fe.payout.presentation.PayoutScreen
import com.example.capyvocab_fe.payout.presentation.PayoutViewModel
import com.example.capyvocab_fe.profile.presentation.ChangePasswordScreen
import com.example.capyvocab_fe.profile.presentation.ProfileScreen
import com.example.capyvocab_fe.profile.presentation.ProfileSettingScreen
import com.example.capyvocab_fe.profile.presentation.ProfileViewModel
import com.example.capyvocab_fe.report.presentation.ReportViewModel
import com.example.capyvocab_fe.report.presentation.UserReportScreen
import com.example.capyvocab_fe.user.community.presentation.CommunityEvent
import com.example.capyvocab_fe.user.community.presentation.CommunityScreen
import com.example.capyvocab_fe.user.community.presentation.CommunityViewModel
import com.example.capyvocab_fe.user.community.presentation.CreatePostScreen
import com.example.capyvocab_fe.user.community.presentation.EditPostScreen
import com.example.capyvocab_fe.user.community.presentation.MyPostScreen
import com.example.capyvocab_fe.user.community.presentation.OwnerPostScreenContent
import com.example.capyvocab_fe.user.community.presentation.PostScreen
import com.example.capyvocab_fe.user.learn.presentation.CourseScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnEvent
import com.example.capyvocab_fe.user.learn.presentation.LearnFlashcardScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnViewModel
import com.example.capyvocab_fe.user.learn.presentation.TopicsInCourseScreen
import com.example.capyvocab_fe.user.navigator.components.UserBottomNavigation
import com.example.capyvocab_fe.user.navigator.components.UserTopBar
import com.example.capyvocab_fe.user.notification.presentation.NotificationScreen
import com.example.capyvocab_fe.user.notification.presentation.NotificationViewModel
import com.example.capyvocab_fe.user.notification.presentation.handler.NotificationActionHandler
import com.example.capyvocab_fe.user.review.presentation.ReviewScreen
import com.example.capyvocab_fe.user.review.presentation.ReviewViewModel
import com.example.capyvocab_fe.user.test.presentation.screens.CommentScreen
import com.example.capyvocab_fe.user.test.presentation.screens.DoQuizScreen
import com.example.capyvocab_fe.user.test.presentation.screens.EditQuestionScreen
import com.example.capyvocab_fe.user.test.presentation.screens.FlashcardLearningScreen
import com.example.capyvocab_fe.user.test.presentation.screens.FlashcardScreen
import com.example.capyvocab_fe.user.test.presentation.screens.QuizScreen
import com.example.capyvocab_fe.user.test.presentation.screens.TestDetailScreen
import com.example.capyvocab_fe.user.test.presentation.screens.TestScreen
import com.example.capyvocab_fe.user.test.presentation.screens.TestSettingScreen
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel

@SuppressLint("StateFlowValueCalledInComposition", "UnrememberedGetBackStackEntry")
@RequiresApi(Build.VERSION_CODES.O)
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
    val notificationActionHandler = remember { NotificationActionHandler(navController) }
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    val learnViewModel: LearnViewModel = hiltViewModel()
    val learnState by learnViewModel.state.collectAsState()

    val communityViewModel: CommunityViewModel = hiltViewModel()
    val communityState by communityViewModel.state.collectAsState()

    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
    val exerciseState by exerciseViewModel.state.collectAsState()
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val reviewState = reviewViewModel.state

    val payOutViewModel: PayoutViewModel = hiltViewModel()
    val reportVM: ReportViewModel = hiltViewModel()
    val reportState by reportVM.state.collectAsState()
    val notificationVM: NotificationViewModel = hiltViewModel()
    val notificationState by notificationVM.state.collectAsState()

    selectedItem = when (backStackState?.destination?.route) {
        Route.UserCommunityScreen.route -> 0
        Route.UserReviewScreen.route -> 1
        Route.UserLearnScreen.route -> 2
        Route.UserTestScreen.route -> 3
        Route.ProfileScreen.route -> 4
        else -> 0
    }

    //hide navbar when in topic, word
    val isBottomVisible = remember(backStackState, reviewState.hasStarted) {
        when (backStackState?.destination?.route) {
            Route.UserReviewScreen.route -> !reviewState.hasStarted // ẩn nếu đang ôn tập
            Route.UserCommunityScreen.route,
            Route.UserLearnScreen.route,
            Route.UserTestScreen.route,
            Route.ProfileScreen.route -> true

            else -> false
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isBottomVisible) UserTopBar(navController, notificationState.unreadCount)
        },
        bottomBar = {
            if (isBottomVisible) {
                UserBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        if (index == selectedItem) return@UserBottomNavigation
                        selectedItem = index
                        when (index) {
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
                                route = Route.ProfileScreen.route
                            )
                        }
                    }
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        val topPadding = it.calculateTopPadding()
        NavHost(
            navController = navController,
            startDestination = Route.UserCommunityScreen.route,
            modifier = Modifier.padding(
                bottom = if (isBottomVisible) bottomPadding else 0.dp,
                top = if (isBottomVisible) topPadding else 0.dp
            )
        ) {
            //user community screen
            composable(route = Route.UserCommunityScreen.route) {
                //TODO: navigate to user home screen

                CommunityScreen(
                    viewModel = communityViewModel,
                    onPostComment = { post ->
                        navController.navigate("${Route.UserPostScreen.route}/${post.id}")
                    },
                    onCreatePost = {
                        navController.navigate("${Route.UserCreatePostScreen.route}")
                    },
                    onClickUserPostsScreen = {user ->
                        navController.navigate("${Route.UserOwnerPostScreen.route}/${user.id}")
                    },
                    onMyPost = {
                        navController.navigate("${Route.UserMyPostScreen.route}")
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

            //CreatePostScreen
            composable(
                route = "${Route.UserCreatePostScreen.route}",
            ) { backStackEntry ->
                CreatePostScreen(
                    viewModel = communityViewModel,
                    navController = navController,
                    onBackClick = {
                        navController.popBackStack();
                        communityViewModel.onEvent(CommunityEvent.LoadPosts);
                    }
                )
            }

            //userPosts
            composable(
                route = "${Route.UserOwnerPostScreen.route}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")

                LaunchedEffect(userId) {
                    communityViewModel.onEvent(CommunityEvent.GetUserByID(userId!!.toInt()))
                }

                communityState.selectUser?.let{user ->
                    OwnerPostScreenContent(
                        user = user,
                        viewModel = communityViewModel,
                        navController = navController,
                        onBackClick = {navController.popBackStack()},
                        onPostComment = { post ->
                            navController.navigate("${Route.UserPostScreen.route}/${post.id}")
                        }
                    )
                }
            }

            //MyPost Screen
            composable(
                route = "${Route.UserMyPostScreen.route}",
            ) { backStackEntry ->

                LaunchedEffect(true) {
                    communityViewModel.onEvent(CommunityEvent.LoadMyUser)
                }
                communityState.selectUser?.let{user ->
                    MyPostScreen (
                        user = user,
                        viewModel = communityViewModel,
                        navController = navController,
                        onBackClick = {navController.popBackStack()},
                        onPostComment = { post ->
                            navController.navigate("${Route.UserPostScreen.route}/${post.id}")
                        },
                        onEditPost = {post ->
                            navController.navigate("${Route.UserEditPostScreen.route}/${post.id}")
                        }
                    )
                }

            }

            //editPostScreen
            composable(
                route = "${Route.UserEditPostScreen.route}/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { backStackEntry ->

                val postId = backStackEntry.arguments?.getInt("postId")

                LaunchedEffect(postId) {
                    communityViewModel.onEvent(CommunityEvent.GetPostById(postId!!.toInt()))
                }

                communityState.selectedPost?.let { post ->
                    EditPostScreen(
                        post = post,
                        viewModel = communityViewModel,
                        navController = navController,
                        onBackClick = { navController.popBackStack() },
                    )
                }

            }

            //user review screen
            composable(route = Route.UserReviewScreen.route) {
                ReviewScreen(
                    viewModel = reviewViewModel,
                    navController = navController
                )
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
                            navController.popBackStack(
                                "${Route.UserWordsScreen.route}/${topic.id}",
                                inclusive = true
                            )
                        },
                        navController = navController
                    )
                }
            }

            //user test screen
            composable(route = Route.UserTestScreen.route) {
                TestScreen(
                    viewModel = exerciseViewModel,
                    navController = navController
                )
            }
            // test setting screen
            composable(
                route = "${Route.TestSettingScreen.route}/{folderId}",
                arguments = listOf(navArgument("folderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId")
                LaunchedEffect(folderId) {
                    exerciseViewModel.onEvent(ExerciseEvent.GetFolderById(folderId!!))
                }
                exerciseState.currentFolder?.let {
                    TestSettingScreen(
                        folder = it,
                        onSaveClick = { updated ->
                            exerciseViewModel.onEvent(ExerciseEvent.UpdateFolder(it.id, updated))
                            navController.popBackStack()
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            // test detail screen
            composable(
                route = "${Route.TestDetailScreen.route}/{folderId}",
                arguments = listOf(navArgument("folderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId")
                LaunchedEffect(folderId) {
                    exerciseViewModel.onEvent(ExerciseEvent.GetFolderById(folderId!!))
                }
                exerciseState.currentFolder?.let { folder ->
                    TestDetailScreen(
                        folder = folder,
                        onBack = {
                            exerciseViewModel.onEvent(ExerciseEvent.ClearCurrentFolder)
                            navController.popBackStack()
                        },
                        onVoteClick = { exerciseViewModel.onEvent(ExerciseEvent.VoteFolder(folder.id)) },
                        onUnVoteClick = { exerciseViewModel.onEvent(ExerciseEvent.UnvoteFolder(folder.id)) },
                        navController = navController
                    )
                }
            }
            // màn hình chỉnh sửa chi tiết câu hỏi
            composable(
                route = "${Route.EditQuestionScreen.route}/{quizId}/{questionIndex}/{folderId}",
                arguments = listOf(
                    navArgument("quizId") { type = NavType.IntType },
                    navArgument("questionIndex") { type = NavType.IntType },
                    navArgument("folderId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val quizId = backStackEntry.arguments?.getInt("quizId") ?: 0
                val questionIndex = backStackEntry.arguments?.getInt("questionIndex") ?: 0
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: 0
                EditQuestionScreen(
                    quizId = quizId,
                    questionIndex = questionIndex,
                    folderId = folderId,
                    navController = navController,
                    viewModel = exerciseViewModel
                )
            }
            // Thêm các route cho TestDetailContent
            composable(
                route = "${Route.QuizScreen.route}/{folderId}",
                arguments = listOf(
                    navArgument("folderId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: -1
                exerciseState.currentQuiz?.let {
                    QuizScreen(
                        navController = navController,
                        quizId = it.id,
                        folderId = folderId,
                        state = exerciseState,
                        onEvent = exerciseViewModel::onEvent
                    )
                }
            }
            composable(
                route = "${Route.DoQuizScreen.route}/{quizId}/{folderId}",
                arguments = listOf(
                    navArgument("quizId") { type = NavType.IntType },
                    navArgument("folderId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val quizId = backStackEntry.arguments?.getInt("quizId") ?: 0
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: 0
                DoQuizScreen(
                    navController = navController,
                    quizId = quizId,
                    folderId = folderId,
                    state = exerciseState,
                    onEvent = exerciseViewModel::onEvent
                )
            }
            composable(
                route = "${Route.FlashCardScreen.route}/{folderId}",
                arguments = listOf(navArgument("folderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: 0
                FlashcardScreen(
                    navController = navController,
                    folderId = folderId,
                    state = exerciseState,
                    onEvent = exerciseViewModel::onEvent
                )
            }
            composable(
                route = "${Route.FlashCardLearningScreen}/{folderId}",
                arguments = listOf(navArgument("folderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: 0
                FlashcardLearningScreen(
                    folderId = folderId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "${Route.CommentScreen.route}/{folderId}",
                arguments = listOf(navArgument("folderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val folderId = backStackEntry.arguments?.getInt("folderId") ?: 0
                LaunchedEffect(folderId) {
                    exerciseViewModel.onEvent(ExerciseEvent.GetFolderById(folderId!!))
                }
                CommentScreen(
                    navController = navController,
                    folderId = folderId,
                    state = exerciseState,
                    onEvent = exerciseViewModel::onEvent
                )

            }

            //user profile screen
            composable(route = Route.ProfileScreen.route) { backStackEntry ->
                val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)
                ProfileScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(route = Route.ProfileSettingScreen.route) {
                val parentEntry = remember {
                    navController.getBackStackEntry(Route.ProfileScreen.route)
                }
                val viewModel = hiltViewModel<ProfileViewModel>(parentEntry)

                ProfileSettingScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(route = Route.ChangePasswordScreen.route) {
                val parentEntry = remember {
                    navController.getBackStackEntry(Route.ProfileScreen.route)
                }
                val viewModel = hiltViewModel<ProfileViewModel>(parentEntry)

                ChangePasswordScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(route = Route.UserPayoutScreen.route) {
                PayoutScreen(
                    navController = navController,
                    viewModel = payOutViewModel
                )
            }
            composable(route = Route.UserReportScreen.route) {
                UserReportScreen(
                    state = reportState,
                    onEvent = reportVM::onEvent,
                    navController = navController
                )
            }
            composable(route = Route.UserNotificationScreen.route) {
                NotificationScreen(
                    viewModel = notificationVM,
                    navController = navController,
                    notificationActionHandler = notificationActionHandler
                )
            }
        }
    }
}

@Composable
fun LoadPosts() {
    TODO("Not yet implemented")
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