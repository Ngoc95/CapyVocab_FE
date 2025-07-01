package com.example.capyvocab_fe.admin.navigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.example.capyvocab_fe.admin.dashboard.presentation.DashboardScreen
import com.example.capyvocab_fe.admin.dashboard.presentation.DashboardViewModel
import com.example.capyvocab_fe.admin.navigator.components.AdminNavigationDrawerContent
import com.example.capyvocab_fe.admin.navigator.components.DrawerNavigationItem
import com.example.capyvocab_fe.admin.topic.presentation.TopicEvent
import com.example.capyvocab_fe.admin.topic.presentation.TopicListViewModel
import com.example.capyvocab_fe.admin.topic.presentation.TopicScreen
import com.example.capyvocab_fe.admin.topic.presentation.TopicsInCourseScreen
import com.example.capyvocab_fe.admin.user.presentation.UserListEvent
import com.example.capyvocab_fe.admin.user.presentation.UserListViewModel
import com.example.capyvocab_fe.admin.user.presentation.UserScreen
import com.example.capyvocab_fe.admin.word.presentation.WordEvent
import com.example.capyvocab_fe.admin.word.presentation.WordListViewModel
import com.example.capyvocab_fe.admin.word.presentation.WordScreen
import com.example.capyvocab_fe.admin.word.presentation.WordsInTopicScreen
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.payout.presentation.AdminPayoutScreen
import com.example.capyvocab_fe.payout.presentation.PayoutScreen
import com.example.capyvocab_fe.payout.presentation.PayoutViewModel
import com.example.capyvocab_fe.profile.presentation.ChangePasswordScreen
import com.example.capyvocab_fe.profile.presentation.ProfileScreen
import com.example.capyvocab_fe.profile.presentation.ProfileSettingScreen
import com.example.capyvocab_fe.profile.presentation.ProfileViewModel
import com.example.capyvocab_fe.report.presentation.AdminReportScreen
import com.example.capyvocab_fe.report.presentation.ReportViewModel
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNavigator(rootNavController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackState?.destination?.route ?: Route.HomeScreen.route

    // Get the viewModels to access multi select state
    val userViewModel: UserListViewModel = hiltViewModel()
    val userListState by userViewModel.state.collectAsState()

    val courseViewModel: CourseListViewModel = hiltViewModel()
    val courseListState by courseViewModel.state.collectAsState()

    val topicViewModel: TopicListViewModel = hiltViewModel()
    val topicListState by topicViewModel.state.collectAsState()

    val wordViewModel: WordListViewModel = hiltViewModel()
    val wordListState by wordViewModel.state.collectAsState()

    val payoutViewModel: PayoutViewModel = hiltViewModel()
    val reportVM: ReportViewModel = hiltViewModel()
    val reportState by reportVM.state.collectAsState()

    // Check if in multi-select mode to adjust UI behavior
    val isInMultiSelectMode = when {
        currentRoute == Route.UsersScreen.route -> userListState.isMultiSelecting
        currentRoute == Route.CoursesScreen.route -> courseListState.isMultiSelecting
        currentRoute == Route.TopicsScreen.route -> topicListState.isMultiSelecting
        currentRoute == Route.WordsScreen.route -> wordListState.isMultiSelecting
        currentRoute.startsWith("${Route.TopicsScreen.route}/") == true -> topicListState.isMultiSelecting
        currentRoute.startsWith("${Route.WordsScreen.route}/") == true -> wordListState.isMultiSelecting
        else -> false
    }

    // Check if current screen is a nested screen
    val isNestedScreen = currentRoute.startsWith("${Route.TopicsScreen.route}/") ||
            currentRoute.startsWith("${Route.WordsScreen.route}/")

    // Get the number of selected items based on current route
    val selectedItemsCount = when {
        currentRoute == Route.UsersScreen.route -> userListState.selectedUsers.size
        currentRoute == Route.CoursesScreen.route -> courseListState.selectedCourses.size
        currentRoute == Route.TopicsScreen.route -> topicListState.selectedTopics.size
        currentRoute == Route.WordsScreen.route -> wordListState.selectedWords.size
        currentRoute.startsWith("${Route.TopicsScreen.route}/") == true -> topicListState.selectedTopics.size
        currentRoute.startsWith("${Route.WordsScreen.route}/") == true -> wordListState.selectedWords.size
        else -> 0
    }

    // Check if current screen should show back button
    val shouldShowBackButton = isNestedScreen || isInMultiSelectMode

    // Generate screen title based on current route
    val screenTitle = when {
        isInMultiSelectMode -> "Đã chọn $selectedItemsCount"
        currentRoute == Route.HomeScreen.route -> "Trang chủ"
        currentRoute == Route.CoursesScreen.route -> "Khoá học"
        currentRoute == Route.TopicsScreen.route -> "Chủ đề"
        currentRoute == Route.WordsScreen.route -> "Từ vựng"
        currentRoute == Route.UsersScreen.route -> "Người dùng"
        currentRoute == Route.ProfileScreen.route -> "Hồ sơ"
        currentRoute.startsWith("${Route.TopicsScreen.route}/") == true ->
            courseListState.selectedCourse?.title ?: "Chủ đề"

        currentRoute.startsWith("${Route.WordsScreen.route}/") == true ->
            topicListState.selectedTopic?.title ?: "Từ vựng"
        currentRoute == Route.AdminPayoutScreen.route -> "Rút tiền"
        currentRoute == Route.AdminReportScreen.route -> "Báo cáo"
        else -> "Admin Panel"
    }
    // Add state for delete confirmation
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Function to handle delete action based on current route
    val handleDeleteSelected = {
        // Show confirmation dialog instead of direct deletion
        showDeleteConfirmDialog = true
    }

    val executeDeleteAction = {
        when {
            currentRoute == Route.UsersScreen.route ->
                userViewModel.onEvent(UserListEvent.OnDeleteSelectedUsers)

            currentRoute == Route.CoursesScreen.route ->
                courseViewModel.onEvent(CourseEvent.OnDeleteSelectedCourses)

            currentRoute == Route.TopicsScreen.route ->
                topicViewModel.onEvent(TopicEvent.OnDeleteSelectedTopics)

            currentRoute == Route.WordsScreen.route ->
                wordViewModel.onEvent(WordEvent.OnDeleteSelectedWords)

            currentRoute.startsWith("${Route.TopicsScreen.route}/") == true ->
                topicViewModel.onEvent(TopicEvent.OnDeleteSelectedTopics)

            currentRoute.startsWith("${Route.WordsScreen.route}/") == true ->
                wordViewModel.onEvent(WordEvent.OnDeleteSelectedWords)
        }
        showDeleteConfirmDialog = false
    }

    // List of navigation items for the drawer
    val navigationItems = listOf(
        DrawerNavigationItem(
            title = "Trang chủ",
            route = Route.HomeScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.admin_home,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.admin_selected_home
        ),
        DrawerNavigationItem(
            title = "Khoá học",
            route = Route.CoursesScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.admin_course,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.admin_selected_course
        ),
        DrawerNavigationItem(
            title = "Chủ đề",
            route = Route.TopicsScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.admin_topic,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.admin_selected_topic
        ),
        DrawerNavigationItem(
            title = "Từ vựng",
            route = Route.WordsScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.admin_word,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.admin_selected_word
        ),
        DrawerNavigationItem(
            title = "Người dùng",
            route = Route.UsersScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.admin_user,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.admin_selected_user
        ),
        DrawerNavigationItem(
            title = "Rút tiền",
            route = Route.AdminPayoutScreen.route,
            iconRes = R.drawable.ic_payout,
            selectedIconRes = R.drawable.ic_selected_payout
        ),
        DrawerNavigationItem(
            title = "Báo cáo",
            route = Route.AdminReportScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.ic_report_ad,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.ic_selected_report_ad
        ),
        DrawerNavigationItem(
            title = "Hồ sơ",
            route = Route.ProfileScreen.route,
            iconRes = com.example.capyvocab_fe.R.drawable.user_profile,
            selectedIconRes = com.example.capyvocab_fe.R.drawable.ic_selected_profile
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(MaterialTheme.dimens.large * 4)
            ) {
                CapyVocab_FETheme(dynamicColor = false)  {
                    AdminNavigationDrawerContent(
                        items = navigationItems,
                        currentRoute = currentRoute,
                        onItemClick = { route ->
                            navigateToTab(navController, route)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        },
        gesturesEnabled = !isInMultiSelectMode && !isNestedScreen
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CapyVocab_FETheme(dynamicColor = false) {
                    if(shouldShowTopBar(currentRoute)){
                        TopAppBar(
                            title = {
                                Text(
                                    text = screenTitle,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            navigationIcon = {
                                if (shouldShowBackButton) {
                                    // show back button for nested screens or multi-select mode
                                    IconButton(
                                        modifier = Modifier.size(MaterialTheme.dimens.medium3),
                                        onClick = {
                                            if (isInMultiSelectMode) {
                                                // handle multi-select back button
                                                when {
                                                    currentRoute == Route.UsersScreen.route -> userViewModel.onEvent(
                                                        UserListEvent.CancelMultiSelect
                                                    )

                                                    currentRoute == Route.CoursesScreen.route -> courseViewModel.onEvent(
                                                        CourseEvent.CancelMultiSelect
                                                    )

                                                    currentRoute == Route.TopicsScreen.route -> topicViewModel.onEvent(
                                                        TopicEvent.CancelMultiSelect
                                                    )

                                                    currentRoute == Route.WordsScreen.route -> wordViewModel.onEvent(
                                                        WordEvent.CancelMultiSelect
                                                    )

                                                    currentRoute.startsWith("${Route.TopicsScreen.route}/") == true ->
                                                        topicViewModel.onEvent(TopicEvent.CancelMultiSelect)

                                                    currentRoute.startsWith("${Route.WordsScreen.route}/") == true ->
                                                        wordViewModel.onEvent(WordEvent.CancelMultiSelect)
                                                }
                                            } else {
                                                //regular back navigation
                                                navController.popBackStack()
                                            }
                                        }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                            contentDescription = "Back",
                                            modifier = Modifier.size(MaterialTheme.dimens.medium1),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } // Only show menu button if not in multi-select mode
                                else if (!isInMultiSelectMode) {
                                    IconButton(
                                        modifier = Modifier.size(MaterialTheme.dimens.medium3),
                                        onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) {
                                                    drawerState.open()
                                                } else {
                                                    drawerState.close()
                                                }
                                            }
                                        }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu",
                                            modifier = Modifier.size(MaterialTheme.dimens.medium1),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            actions = {
                                //show delete button when in multi-select mode
                                if (isInMultiSelectMode && selectedItemsCount > 0) {
                                    IconButton(
                                        modifier = Modifier.size(MaterialTheme.dimens.medium3),
                                        onClick = { handleDeleteSelected() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete selected",
                                            modifier = Modifier.size(MaterialTheme.dimens.medium1),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = if(shouldShowTopBar(currentRoute)) Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                else Modifier.fillMaxSize().padding(top = 0.dp)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Route.HomeScreen.route
                ) {
                    // Home screen
                    composable(route = Route.HomeScreen.route) {
                        val dashboardViewModel: DashboardViewModel = hiltViewModel()
                        DashboardScreen(viewModel = dashboardViewModel)
                    }

                    // Courses screen
                    composable(route = Route.CoursesScreen.route) {
                        CourseScreen(
                            onCourseClick = { course ->
                                navController.navigate("${Route.TopicsScreen.route}/${course.id}")
                            },
                            viewModel = courseViewModel,
                            navController = navController
                        )
                    }

                    // Topics in course screen
                    composable(
                        route = "${Route.TopicsScreen.route}/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getInt("courseId")
                        LaunchedEffect(courseId) {
                            courseViewModel.onEvent(CourseEvent.GetCourseById(courseId!!.toInt()))
                        }
                        courseListState.selectedCourse?.let { course ->
                            TopicsInCourseScreen(
                                course = course,
                                onTopicClick = { topic ->
                                    navController.navigate("${Route.WordsScreen.route}/${topic.id}")
                                },
                                viewModel = topicViewModel,
                                navController = navController
                            )
                        }
                    }

                    // Topics screen
                    composable(route = Route.TopicsScreen.route) {
                        TopicScreen(
                            onTopicClick = { topic ->
                                navController.navigate("${Route.WordsScreen.route}/${topic.id}")
                            },
                            viewModel = topicViewModel,
                            navController = navController
                        )
                    }

                    // Words in topic screen
                    composable(
                        route = "${Route.WordsScreen.route}/{topicId}",
                        arguments = listOf(navArgument("topicId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val topicId = backStackEntry.arguments?.getInt("topicId")
                        LaunchedEffect(topicId) {
                            topicViewModel.onEvent(TopicEvent.GetTopicById(topicId!!.toInt()))
                        }
                        topicListState.selectedTopic?.let { topic ->
                            WordsInTopicScreen(
                                topic = topic,
                                viewModel = wordViewModel,
                                navController = navController
                            )
                        }
                    }

                    // Words screen
                    composable(route = Route.WordsScreen.route) {
                        WordScreen(
                            viewModel = wordViewModel,
                            navController = navController
                        )
                    }

                    // User screen
                    composable(route = Route.UsersScreen.route) {
                        UserScreen(
                            state = userListState,
                            navController = navController,
                            onEvent = userViewModel::onEvent
                        )
                    }

                    //payout screen
                    composable(route = Route.AdminPayoutScreen.route) {
                        AdminPayoutScreen(
                            viewModel = payoutViewModel
                        )
                    }
                    composable(route = Route.UserPayoutScreen.route) {
                        PayoutScreen(
                            navController = navController,
                            viewModel = payoutViewModel
                        )
                    }

                    // Report screen
                    composable(route = Route.AdminReportScreen.route) {
                        AdminReportScreen(
                            state = reportState,
                            onEvent = reportVM::onEvent
                        )
                    }

                    // Profile screen
                    composable(route = Route.ProfileScreen.route) { backStackEntry ->
                        val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)
                        ProfileScreen(
                            viewModel = viewModel,
                            navController = navController,
                            rootNavController = rootNavController,
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
                }
            }
        }
    }
    // Add confirmation dialog for multi select
    if (showDeleteConfirmDialog) {
        val itemType = when {
            currentRoute == Route.UsersScreen.route -> "người dùng"
            currentRoute == Route.CoursesScreen.route -> "khóa học"
            currentRoute == Route.TopicsScreen.route -> "chủ đề"
            currentRoute == Route.WordsScreen.route -> "từ vựng"
            currentRoute.startsWith("${Route.TopicsScreen.route}/") -> "chủ đề"
            currentRoute.startsWith("${Route.WordsScreen.route}/") -> "từ vựng"
            else -> "mục"
        }

        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá $selectedItemsCount $itemType đã chọn không?",
            onConfirm = {
                executeDeleteAction()
            },
            onDismiss = {
                showDeleteConfirmDialog = false
            }
        )
    }
}

fun shouldShowTopBar(currentRoute: String): Boolean {
    return currentRoute != Route.ProfileSettingScreen.route &&
            currentRoute != Route.ChangePasswordScreen.route &&
            currentRoute != Route.UserPayoutScreen.route
}

private fun navigateToTab(navController: NavController, route: String) {
    // Only navigate if it's a top-level destination
    // We don't want to navigate to routes with parameters from the drawer
    if (route == Route.HomeScreen.route ||
        route == Route.CoursesScreen.route ||
        route == Route.TopicsScreen.route ||
        route == Route.WordsScreen.route ||
        route == Route.UsersScreen.route ||
        route == Route.AdminPayoutScreen.route ||
        route == Route.AdminReportScreen.route ||
        route == Route.ProfileScreen.route
    ) {
        navController.navigate(route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}