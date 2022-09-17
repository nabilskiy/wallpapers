package wallgram.hd.wallpapers.presentation.wallpaper.info

import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class InfoAdapter : GenericAdapter.Base(
    ProgressViewHolderChain(
        InfoViewHolderChain(
            TagsViewHolderChain(
                ViewHolderFactoryChain.Exception()
            )
        )
    )
)