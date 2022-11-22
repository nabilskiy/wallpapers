package wallgram.hd.wallpapers.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.presentation.subscribe.Subscribe

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    @ActivityScoped
    fun provideSubscribe(
        @ActivityContext context: Context,
        billingRepository: BillingRepository
    ): Subscribe = Subscribe.Base(context, billingRepository)

}