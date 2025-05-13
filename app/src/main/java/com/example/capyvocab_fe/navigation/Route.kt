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
    object UserHomeScreen : Route(route = "userHomeScreen")
    object UserLearnScreen: Route(route = "userLearnScreen")
    object UserCommunityScreen: Route(route = "userCommunityScreen")
    object UserTestScreen: Route(route = "userTestScreen")
    object UserProfileScreen: Route(route = "userProfileScreen")
}