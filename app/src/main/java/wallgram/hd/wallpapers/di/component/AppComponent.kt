package wallgram.hd.wallpapers.di.component

import android.app.Application
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.di.ActivityModuleBuilder
import wallgram.hd.wallpapers.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.BindsInstance
import wallgram.hd.wallpapers.di.module.ErrorModule
import wallgram.hd.wallpapers.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            DataModule::class,
            ErrorModule::class,
            ActivityModuleBuilder::class,
            ViewModelModule::class
        ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: wallgram.hd.wallpapers.App)
}