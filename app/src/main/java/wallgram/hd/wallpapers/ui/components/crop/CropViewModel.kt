package wallgram.hd.wallpapers.ui.components.crop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.repository.data.DataRepositorySource
import wallgram.hd.wallpapers.model.Pic
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.util.modo.back
import javax.inject.Inject

class CropViewModel @Inject constructor(
        private val dataRepository: DataRepositorySource
): BaseViewModel(){

    private val picLiveDataPrivate = MutableLiveData<Resource<Pic>>()
    val picLiveData: LiveData<Resource<Pic>> get() = picLiveDataPrivate

    fun getPic(id: Int, res: String){
        viewModelScope.launch {
            picLiveDataPrivate.value = Resource.Loading()

            dataRepository.getPic(id, res).collect {
                picLiveDataPrivate.value = it
            }

        }
    }

    fun onBack() {
        modo.back()
    }
}