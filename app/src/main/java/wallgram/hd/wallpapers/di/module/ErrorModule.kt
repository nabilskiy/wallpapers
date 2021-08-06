package wallgram.hd.wallpapers.di.module

import dagger.Binds
import dagger.Module
import wallgram.hd.wallpapers.data.error.mapper.ErrorMapper
import wallgram.hd.wallpapers.data.error.mapper.ErrorMapperInterface
import wallgram.hd.wallpapers.usecase.errors.ErrorFactory
import wallgram.hd.wallpapers.usecase.errors.ErrorManager
import javax.inject.Singleton

@Module
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorFactory

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperInterface
}