package wallgram.hd.wallpapers.presentation.settings.resolution

import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class ResolutionsAdapter : GenericAdapter.Base(
    ResolutionViewHolderFactoryChain(
        ViewHolderFactoryChain.Exception()
    )
)