package com.example.capyvocab_fe.admin.course.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course

sealed class CourseEvent {
    object LoadCourses : CourseEvent()
    object LoadMoreCourses : CourseEvent()
    data class SaveCourse(
        val course: Course
    ) : CourseEvent()
    data class DeleteCourse(val courseId: Int) : CourseEvent()
    data class GetCourseById(val courseId: Int): CourseEvent()

    data class OnCourseLongPress(val courseId: Int) : CourseEvent()
    data class OnCourseSelectToggle(val courseId: Int) : CourseEvent()
    object OnSelectAllToggle : CourseEvent()
    object OnDeleteSelectedCourses : CourseEvent()
    object CancelMultiSelect : CourseEvent()
}
