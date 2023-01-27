package wallgram.hd.wallpapers.presentation.dialogs

import wallgram.hd.wallpapers.R

sealed class DownloadAction {


    class Home() : DownloadAction()

    class Lock : DownloadAction()

    class Both : DownloadAction()

    class Download : DownloadAction()

    class DownloadSource: DownloadAction()

}

//enum class DownloadAction {
//    HOME_SCREEN, LOCK_SCREEN, BOTH_SCREEN, DOWNLOAD
//}