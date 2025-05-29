package com.example.capyvocab_fe.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.admin.user.data.remote.AdminUserApi
import com.example.capyvocab_fe.admin.word.data.remote.AdminWordApi
import com.example.capyvocab_fe.auth.data.remote.AuthApi
import com.example.capyvocab_fe.auth.data.repository.AuthRepositoryImpl
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.core.data.TokenManager
import com.example.capyvocab_fe.core.network.AuthInterceptor
import com.example.capyvocab_fe.user.learn.data.remote.UserLearnApi
import com.example.capyvocab_fe.user.review.data.remote.UserReviewApi
import com.example.capyvocab_fe.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAdminUserApi(retrofit: Retrofit): AdminUserApi {
        return retrofit.create(AdminUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminWordApi(retrofit: Retrofit): AdminWordApi {
        return retrofit.create(AdminWordApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminCourseApi(retrofit: Retrofit): AdminCourseApi {
        return retrofit.create(AdminCourseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminTopicApi(retrofit: Retrofit): AdminTopicApi {
        return retrofit.create(AdminTopicApi::class.java)
    }

    // USER
    @Provides
    @Singleton
    fun provideUserLearnApi(retrofit: Retrofit): UserLearnApi {
        return retrofit.create(UserLearnApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserReviewApi(retrofit: Retrofit): UserReviewApi {
        return retrofit.create(UserReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.filesDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50MB
                    .build()
            }
            .build()
    }
}