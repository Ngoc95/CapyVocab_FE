package com.example.capyvocab_fe.navigation

sealed class Route(
    val route: String
) {
    //auth navigator
    object AuthNavigation : Route(route = "authNavigation")
    object LoginScreen : Route(route = "loginScreen")
    object RegisterScreen : Route(route = "registerScreen")

    //admin navigator
    object AdminNavigation : Route(route = "adminNavigation")
    object AdminNavigator : Route(route = "adminNavigator")
    object HomeScreen : Route(route = "homeScreen")
    object CoursesScreen : Route(route = "coursesScreen")
    object TopicsScreen: Route(route = "topicsScreen")
    object WordsScreen: Route(route = "wordsScreen")
    object UsersScreen : Route(route = "usersScreen")
    object SettingScreen : Route(route = "settingScreen")

    //user navigator
    object UserNavigation : Route(route = "userNavigation")
    object UserNavigator : Route(route = "userNavigator")
    object UserCommunityScreen: Route(route = "userCommunityScreen")
    object UserPostScreen: Route(route = "userPostScreen")
    object UserReviewScreen : Route(route = "userReviewScreen")
    object UserLearnScreen: Route(route = "userLearnScreen")
    object UserCoursesScreen : Route(route = "userCoursesScreen")
    object UserTopicsScreen: Route(route = "userTopicsScreen")
    object UserWordsScreen: Route(route = "userWordsScreen")
    object UserTestScreen: Route(route = "userTestScreen")
    object UserProfileScreen: Route(route = "userProfileScreen")
}