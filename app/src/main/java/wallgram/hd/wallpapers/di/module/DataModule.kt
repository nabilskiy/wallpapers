package wallgram.hd.wallpapers.di.module

import dagger.Binds
import dagger.Module
import wallgram.hd.wallpapers.data.repository.DataRepository
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.data.repository.billing.BillingRepository
import wallgram.hd.wallpapers.data.repository.billing.BillingRepositorySource
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataRepositorySource

    @Binds
    @Singleton
    abstract fun provideBillingRepository(billingRepository: BillingRepository): BillingRepositorySource

}