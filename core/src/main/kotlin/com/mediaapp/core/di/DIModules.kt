package com.mediaapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mediaapp.core.BuildConfig
import com.mediaapp.core.api.MediaServiceApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class MediaModule {

    @Provides
    @Named("apiKey")
    fun provideApiKey(): String = BuildConfig.API_KEY
}

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMediaService(@Named("baseUrl") baseUrl: String): MediaServiceApi {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MediaServiceApi::class.java)
    }

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl() = BuildConfig.JAMENDO_API
}

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): DatabaseReference =
        FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DATABASE_URL).reference
}