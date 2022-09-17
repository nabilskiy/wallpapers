package wallgram.hd.wallpapers.presentation.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.util.modo.*

abstract class BaseViewModel(
    private val dispatchers: Dispatchers
) : ViewModel() {

    val modo = wallgram.hd.wallpapers.App.modo

    open fun back(){
        modo.back()
    }

    fun showScreen(screen: AppScreen) {
        modo.forward(screen)
    }

    fun showScreen(screen: ExternalScreen) {
        modo.launch(screen)
    }

    protected fun <T> handle(
        block: suspend () -> T
    ) = dispatchers.launchBackground(viewModelScope) {
        block.invoke()
    }

}