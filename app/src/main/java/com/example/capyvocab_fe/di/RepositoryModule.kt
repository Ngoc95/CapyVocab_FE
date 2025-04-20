package com.example.capyvocab_fe.di

import com.example.capyvocab_fe.admin.user.data.repository.AdminUserRepositoryImpl
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.admin.word.data.repository.AdminWordRepositoryImpl
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import com.example.capyvocab_fe.auth.data.repository.AuthRepositoryImpl
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAdminUserRepository(impl: AdminUserRepositoryImpl): AdminUserRepository

    @Binds
    @Singleton
    abstract fun bindAdminWordRepository(impl: AdminWordRepositoryImpl): AdminWordRepository
}
