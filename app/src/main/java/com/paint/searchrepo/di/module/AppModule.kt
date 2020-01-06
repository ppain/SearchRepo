package com.paint.searchrepo.di.module


import com.paint.searchrepo.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class
    ]
)
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(): App {
        return App.instance
    }
}