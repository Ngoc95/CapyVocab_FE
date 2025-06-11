package com.example.capyvocab_fe.navigation

sealed class Route(
    val route: String
) {
    //auth navigator
    object AuthNavigation : Route(route = "authNavigation")
    object LoginScreen : Route(route = "loginScreen")
    object RegisterScreen : Route(route = "registerScreen")
    object OtpScreen: Route(route = "otpScreen")

    //admin navigator
    object AdminNavigation : Route(route = "adminNavigation")
    object AdminNavigator : Route(route = "adminNavigator")
    object HomeScreen : Route(route = "homeScreen")
    object CoursesScreen : Route(route = "coursesScreen")
    object TopicsScreen: Route(route = "topicsScreen")
    object WordsScreen: Route(route = "wordsScreen")
    object UsersScreen : Route(route = "usersScreen")
    object ProfileScreen : Route(route = "profileScreen")

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
    object TestSettingScreen: Route(route = "testSettingScreen")
    object TestDetailScreen: Route(route = "testDetailScreen")
    object FlashCardScreen: Route(route = "flashCardScreen")
    object FlashCardLearningScreen: Route(route = "flashCardLearningScreen")
    object QuizScreen: Route(route = "quizScreen")
    object CommentScreen: Route(route = "commentScreen")
    object EditQuestionScreen : Route(route = "editQuestionScreen")
    object DoQuizScreen: Route(route = "doQuizScreen")
    object UserProfileScreen: Route(route = "userProfileScreen")
    object UserCreatePostScreen: Route(route = "userCreatePostScreen")
    object UserOwnerPostScreen: Route(route = "userOwnerPostScreen")
    object UserAccountSettingScreen: Route(route = "userAccountSettingScreen")
    object UserMyPostScreen: Route(route = "userMyPostScreen")
    object UserEditPostScreen: Route(route = "userEditPostScreen")
}