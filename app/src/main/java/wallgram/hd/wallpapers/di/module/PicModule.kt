package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder
import wallgram.hd.wallpapers.core.domain.HandleDomainError
import wallgram.hd.wallpapers.data.gallery.BaseGalleryRepository
import wallgram.hd.wallpapers.data.gallery.GalleryCloud
import wallgram.hd.wallpapers.data.gallery.GalleryCloudDataSource
import wallgram.hd.wallpapers.data.gallery.ProvideGalleryService
import wallgram.hd.wallpapers.data.pic.BasePicRepository
import wallgram.hd.wallpapers.data.pic.PicCloud
import wallgram.hd.wallpapers.data.pic.PicCloudDataSource
import wallgram.hd.wallpapers.data.pic.ProvidePicService
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.pic.PicDomain
import wallgram.hd.wallpapers.domain.pic.PicInteractor
import wallgram.hd.wallpapers.domain.pic.PicRepository
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.pic.PicUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoListUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.NavigateTag
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class PicModule {

    @Provides
    @ViewModelScoped
    fun providePicService(retrofitBuilder: ProvideRetrofitBuilder): ProvidePicService =
        ProvidePicService.Base(retrofitBuilder)

    @Provides
    @ViewModelScoped
    fun providePicCloudDataSource(
        picService: ProvidePicService,
        handleError: HandleError
    ): PicCloudDataSource =
        PicCloudDataSource.Base(picService.picService(), handleError)

    @Provides
    @ViewModelScoped
    fun providePicRepository(picCloudDataSource: PicCloudDataSource): PicRepository =
        BasePicRepository(picCloudDataSource, PicCloud.Mapper.Base())

    @Provides
    @ViewModelScoped
    fun providePicMapper(): PicDomain.Mapper<PicUi> = PicDomain.Mapper.Base()

    @Provides
    @ViewModelScoped
    fun provideInfoMapper(
        navigateTag: NavigateTag,
        resourceProvider: ResourceProvider
    ): PicDomain.Mapper<InfoListUi> = PicDomain.Mapper.Info(navigateTag, resourceProvider)

    @Provides
    @ViewModelScoped
    fun provideNavigateTag(): NavigateTag = NavigateTag.Base()

    @Provides
    @ViewModelScoped
    fun providePicInteractor(
        mapper: PicDomain.Mapper<PicUi>,
        infoMapper: PicDomain.Mapper<InfoListUi>,
        picRepository: PicRepository,
        dispatchers: Dispatchers,
        handleError: HandleDomainError
    ): PicInteractor =
        PicInteractor.Base(mapper, infoMapper, picRepository, dispatchers, handleError)

}