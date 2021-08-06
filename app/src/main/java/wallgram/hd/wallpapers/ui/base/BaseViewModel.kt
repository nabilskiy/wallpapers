package wallgram.hd.wallpapers.ui.base

import androidx.lifecycle.ViewModel
import wallgram.hd.wallpapers.usecase.errors.ErrorManager
import wallgram.hd.wallpapers.data.error.mapper.ErrorMapper

abstract class BaseViewModel : ViewModel() {

    val modo = wallgram.hd.wallpapers.App.modo

    val errorManager: ErrorManager = ErrorManager(ErrorMapper())

}

interface BaseViewState

interface BaseViewEffect

interface BaseEvent

interface BaseResult