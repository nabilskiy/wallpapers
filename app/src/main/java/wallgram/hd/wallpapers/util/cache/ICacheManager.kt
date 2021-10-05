package wallgram.hd.wallpapers.util.cache

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import wallgram.hd.wallpapers.model.Gallery

interface ICacheManager {

    var wallpapersData: MutableLiveData<PagingData<Gallery>>
    var similarData: MutableLiveData<PagingData<Gallery>>

}