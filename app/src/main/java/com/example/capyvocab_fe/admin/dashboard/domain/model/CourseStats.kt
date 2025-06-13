package com.example.capyvocab_fe.admin.dashboard.domain.model

data class CourseStats(
    val totalCourses: Int,
    val courseCountByLevel: List<CourseLevelCount>,
    val progressStats: CourseProgressStats,
    val topCourses: TopCourses
)

data class CourseLevelCount(
    val level: String,
    val count: Int
)

data class CourseProgressStats(
    val completed: Double,
    val inProgress: Double,
    val notStarted: Double
)

data class TopCourses(
    val topCourses: List<TopCourse>
)

data class TopCourse(
    val courseId: Int,
    val learnerCount: Int
)

data class TopCourseDetail(
    val title: String,
    val level: String,
    val learnerCount: Int
)