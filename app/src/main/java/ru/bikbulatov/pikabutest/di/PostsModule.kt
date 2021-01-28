package ru.bikbulatov.pikabutest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ru.bikbulatov.pikabutest.data.PostsRepoImpl
import ru.bikbulatov.pikabutest.data.api.IPostsApi
import ru.bikbulatov.pikabutest.domain.PostRepo
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PostsModule {
    @Provides
    @Singleton
    fun createPostsApi(postApi: IPostsApi): PostRepo {
        return PostsRepoImpl(postApi)
    }
}