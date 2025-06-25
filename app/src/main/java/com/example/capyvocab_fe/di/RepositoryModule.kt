package com.example.capyvocab_fe.di

import com.example.capyvocab_fe.admin.course.data.repository.AdminCourseRepositoryImpl
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import com.example.capyvocab_fe.admin.dashboard.data.repository.DashboardRepositoryImpl
import com.example.capyvocab_fe.admin.dashboard.domain.repository.DashboardRepository
import com.example.capyvocab_fe.admin.topic.data.repository.AdminTopicRepositoryImpl
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import com.example.capyvocab_fe.admin.user.data.repository.AdminUserRepositoryImpl
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.admin.word.data.repository.AdminWordRepositoryImpl
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import com.example.capyvocab_fe.auth.data.repository.AuthRepositoryImpl
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.payout.data.repository.PayoutRepositoryImpl
import com.example.capyvocab_fe.payout.domain.repository.PayoutRepository
import com.example.capyvocab_fe.report.data.repository.ReportRepositoryImpl
import com.example.capyvocab_fe.report.domain.repository.ReportRepository
import com.example.capyvocab_fe.user.community.data.repository.UserCommunityRepositoryImpl
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import com.example.capyvocab_fe.user.learn.data.repository.UserLearnRepositoryImpl
import com.example.capyvocab_fe.user.learn.domain.repository.UserLearnRepository
import com.example.capyvocab_fe.user.notification.data.repository.NotificationRepositoryImpl
import com.example.capyvocab_fe.user.notification.domain.repository.NotificationRepository
import com.example.capyvocab_fe.user.payment.data.repository.PaymentRepositoryImpl
import com.example.capyvocab_fe.user.payment.domain.repository.PaymentRepository
import com.example.capyvocab_fe.user.profile.data.repository.UserProfileRepositoryImpl
import com.example.capyvocab_fe.user.profile.domain.repository.UserProfileRepository
import com.example.capyvocab_fe.user.review.data.repository.UserReviewRepositoryImpl
import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import com.example.capyvocab_fe.user.test.data.repository.ExerciseRepositoryImpl
import com.example.capyvocab_fe.user.test.domain.repository.ExerciseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // ADMIN
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAdminDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository

    @Binds
    @Singleton
    abstract fun bindAdminUserRepository(impl: AdminUserRepositoryImpl): AdminUserRepository

    @Binds
    @Singleton
    abstract fun bindAdminCourseRepository(impl: AdminCourseRepositoryImpl): AdminCourseRepository

    @Binds
    @Singleton
    abstract fun bindAdminTopicRepository(impl: AdminTopicRepositoryImpl): AdminTopicRepository

    @Binds
    @Singleton
    abstract fun bindAdminWordRepository(impl: AdminWordRepositoryImpl): AdminWordRepository

    // USER
    @Binds
    @Singleton
    abstract fun bindUserLearnRepository(impl: UserLearnRepositoryImpl): UserLearnRepository

    @Binds
    @Singleton
    abstract fun bindUserCommunityRepository(impl: UserCommunityRepositoryImpl): UserCommunityRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository
  
    @Binds
    @Singleton
    abstract fun bindUserExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(impl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindPayoutRepository(impl: PayoutRepositoryImpl): PayoutRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(impl: ReportRepositoryImpl): ReportRepository

    @Binds
    @Singleton
    abstract fun bindUserReviewRepository(impl: UserReviewRepositoryImpl): UserReviewRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
