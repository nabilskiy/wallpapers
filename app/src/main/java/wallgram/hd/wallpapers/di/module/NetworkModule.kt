package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import wallgram.hd.wallpapers.BuildConfig
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.core.data.ProvideConverterFactory
import wallgram.hd.wallpapers.core.data.ProvideInterceptor
import wallgram.hd.wallpapers.core.data.ProvideOkHttpClientBuilder
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.data.filters.FiltersService
import wallgram.hd.wallpapers.data.pic.PicService
import wallgram.hd.wallpapers.data.remote.LanguageInterceptor
import wallgram.hd.wallpapers.data.remote.ResolutionInterceptor
import wallgram.hd.wallpapers.data.search.SearchService
import wallgram.hd.wallpapers.data.tags.TagsService
import wallgram.hd.wallpapers.data.gallery.GalleryService
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://wallspic.com/rest/v3/"

    @Provides
    @Singleton
    fun provideInterceptor(): ProvideInterceptor =
        if (BuildConfig.DEBUG) ProvideInterceptor.Debug() else ProvideInterceptor.Release()

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(interceptor: ProvideInterceptor): ProvideOkHttpClientBuilder =
        ProvideOkHttpClientBuilder.Base(interceptor)

    @Provides
    @Singleton
    fun provideConverterFactory(): ProvideConverterFactory = ProvideConverterFactory.Base()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        httpClientBuilder: ProvideOkHttpClientBuilder,
        converterFactory: ProvideConverterFactory
    ): ProvideRetrofitBuilder = ProvideRetrofitBuilder.Base(httpClientBuilder, converterFactory)

    @Provides
    @Singleton
    fun provideCategoriesService(retrofitBuilder: ProvideRetrofitBuilder): FiltersService {
        return retrofitBuilder.provideRetrofitBuilder().build().create(FiltersService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofitBuilder: ProvideRetrofitBuilder): SearchService {
        return retrofitBuilder.provideRetrofitBuilder().build().create(SearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideTagsService(retrofitBuilder: ProvideRetrofitBuilder): TagsService {
        return retrofitBuilder.provideRetrofitBuilder().build().create(TagsService::class.java)
    }

    @Provides
    @Singleton
    fun providePicService(retrofitBuilder: ProvideRetrofitBuilder): PicService {
        return retrofitBuilder.provideRetrofitBuilder().build().create(PicService::class.java)
    }

    @Provides
    @Singleton
    fun provideWallpapersService(
        retrofitBuilder: ProvideRetrofitBuilder
    ): GalleryService = retrofitBuilder.provideRetrofitBuilder().build().create(GalleryService::class.java)


    @Provides
    @Singleton
    fun provideLanguageInterceptor(locale: LocalizationApplicationDelegate): Interceptor =
        LanguageInterceptor(locale)

    @Provides
    @Singleton
    fun provideResolutionInterceptor(displayProvider: DisplayProvider): Interceptor =
        ResolutionInterceptor(displayProvider)

}
