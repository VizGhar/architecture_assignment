package xyz.kandrac.assignment.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.kandrac.assignment.model.AppNotificator
import xyz.kandrac.assignment.model.ScratchCardProvider
import xyz.kandrac.assignment.net.service.ScratchCardService
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScratchCardApplicationModule {

    @Provides
    @Singleton
    fun provideNotificator(): AppNotificator =
        AppNotificator()

    @Provides
    @Singleton
    fun provideScratchCardProvider(service: ScratchCardService, notificator: AppNotificator): ScratchCardProvider =
        ScratchCardProvider(UUID.randomUUID().toString(), service, notificator)

}