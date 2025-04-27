package com.example.capyvocab_fe.admin.course.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course

sealed class CourseEvent {
    object LoadCourses : CourseEvent()
    data class CreateCourse(val course: Course) : CourseEvent()
    data class UpdateCourse(val course: Course) : CourseEvent()
    data class DeleteCourse(val courseId: Int) : CourseEvent()
}
