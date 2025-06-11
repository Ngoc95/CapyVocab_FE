package com.example.capyvocab_fe.di

import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import com.example.capyvocab_fe.user.review.domain.usecase.GetProgressSummaryUseCase
import com.example.capyvocab_fe.user.review.domain.usecase.GetReviewWordsUseCase
import com.example.capyvocab_fe.user.review.domain.usecase.UpdateWordProgressUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReviewUseCaseModule {

    @Provides
    fun provideGetReviewWordsUseCase(
        repository: UserReviewRepository
    ): GetReviewWordsUseCase = GetReviewWordsUseCase(repository)

    @Provides
    fun provideUpdateWordProgressUseCase(
        repository: UserReviewRepository
    ): UpdateWordProgressUseCase = UpdateWordProgressUseCase(repository)

    @Provides
    fun provideGetProgressSummaryUseCase(
        repository: UserReviewRepository
    ): GetProgressSummaryUseCase = GetProgressSummaryUseCase(repository)
}
