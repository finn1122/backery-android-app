package com.example.mybakery.di

import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient
import com.example.mybakery.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitClient.apiService

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository = UserRepository()
}