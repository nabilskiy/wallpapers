package wallgram.hd.wallpapers.ui.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wallgram.hd.wallpapers.usecase.errors.ErrorManager
import wallgram.hd.wallpapers.data.error.mapper.ErrorMapper

abstract class BaseViewModel : ViewModel() {

    val bundleFragment = MutableLiveData<Bundle>()

    val modo = wallgram.hd.wallpapers.App.modo

    val errorManager: ErrorManager = ErrorManager(ErrorMapper())

}

interface BaseViewState

interface BaseViewEffect

interface BaseEvent

interface BaseResult