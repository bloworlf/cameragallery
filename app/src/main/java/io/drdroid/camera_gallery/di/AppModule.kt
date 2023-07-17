package io.drdroid.camera_gallery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.drdroid.camera_gallery.data.impl.MediaCallImpl
import io.drdroid.camera_gallery.data.repo.Repository
import io.drdroid.camera_gallery.data.impl.RepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

//    @Provides
//    fun providesRetrofitInstance(/*client: OkHttpClient*/): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("")
////            .addConverterFactory(GsonConverterFactory.create())
////            .client(client)
//            .build()
//    }
//
//    @Provides
//    fun providesMediaCall(retrofit: Retrofit): MediaCall {
//        return retrofit.create(MediaCall::class.java)
//    }

//    @Provides
//    fun providesMediaCall(): MediaCall {
//        return MediaCall
//    }

//    @Inject
//    lateinit var mediaCall: MediaCallImpl

    @Provides
    fun provideMediaCall(): MediaCallImpl {
        return MediaCallImpl()
    }

    @Provides
    fun provideRepo(mediaCall: MediaCallImpl): Repository {
        return RepositoryImpl(mediaCall)
    }
}