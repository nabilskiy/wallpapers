package wallgram.hd.wallpapers.ui.details

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import retrofit2.http.Url
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.util.download.download
import java.net.URL
import javax.inject.Inject

class DownloadViewModel @Inject constructor(): BaseViewModel(){

        fun download(url: String){

                url.download().observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = {progress ->

                                },
                                onComplete = {

                                },
                                onError = {}
                        )

        }

}