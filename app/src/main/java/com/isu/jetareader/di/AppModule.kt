package com.isu.jetareader.di

import com.google.firebase.firestore.FirebaseFirestore
import com.isu.jetareader.network.BookApi
import com.isu.jetareader.repository.FireRepository
import com.isu.jetareader.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFireBookRepository() =
        FireRepository(
            queryBook = FirebaseFirestore
                .getInstance()
                .collection("books")
        )

}