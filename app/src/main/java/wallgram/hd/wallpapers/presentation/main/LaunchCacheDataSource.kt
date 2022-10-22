package wallgram.hd.wallpapers.presentation.main

import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite

interface LaunchCacheDataSource : ChangeFirstLaunch, IsFirstLaunch {

    class Base(private val launch: FirstLaunch.Mutable) : LaunchCacheDataSource {

        override fun changeFirstLaunch() = launch.save(false)

        override fun isFirstLaunch() = launch.read()

    }
}