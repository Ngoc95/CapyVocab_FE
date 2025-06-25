package com.example.capyvocab_fe.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.dashboard.data.remote.DashboardApi
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.admin.user.data.remote.AdminUserApi
import com.example.capyvocab_fe.admin.word.data.remote.AdminWordApi
import com.example.capyvocab_fe.auth.data.remote.AuthApi
import com.example.capyvocab_fe.core.network.AuthInterceptor
import com.example.capyvocab_fe.core.network.TokenAuthenticator
import com.example.capyvocab_fe.payout.data.remote.PayoutApi
import com.example.capyvocab_fe.report.data.remote.ReportApi
import com.example.capyvocab_fe.user.community.data.remote.UserCommunityApi
import com.example.capyvocab_fe.user.learn.data.remote.UserLearnApi
import com.example.capyvocab_fe.user.notification.data.remote.NotificationApi
import com.example.capyvocab_fe.user.payment.data.remote.PaymentApi
import com.example.capyvocab_fe.user.profile.data.remote.UserProfileApi
import com.example.capyvocab_fe.user.review.data.remote.UserReviewApi
import com.example.capyvocab_fe.user.test.data.remote.ExerciseApi
import com.example.capyvocab_fe.util.Constant.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

class DateDeserializer : JsonDeserializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        return try {
            json?.asString?.let { dateString ->
                dateFormat.parse(dateString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
    }

    @Provides
    @Singleton
    fun provideSocketIO(): Socket? {
        return try {
            val options = IO.Options()
            IO.socket(BASE_URL, options)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Provides
    @Singleton
    @AuthClient
    fun provideAuthOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(@AuthClient authOkHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@AuthRetrofit authRetrofit: Retrofit): AuthApi {
        return authRetrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAdminDashboardApi(retrofit: Retrofit): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
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
    fun provideUserCommunityApi(retrofit: Retrofit): UserCommunityApi {
        return retrofit.create(UserCommunityApi::class.java)
    }
    @Provides
    @Singleton
    fun provideUserExerciseApi(retrofit: Retrofit): ExerciseApi {
        return retrofit.create(ExerciseApi::class.java)
    }

    @Provides
    @Singleton

    fun provideUserProfileApi(retrofit: Retrofit): UserProfileApi {
        return retrofit.create(UserProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApi {
        return retrofit.create(PaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun providePayoutApi(retrofit: Retrofit): PayoutApi {
        return retrofit.create(PayoutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserReviewApi(retrofit: Retrofit): UserReviewApi {
        return retrofit.create(UserReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReportApi(retrofit: Retrofit): ReportApi {
        return retrofit.create(ReportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
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