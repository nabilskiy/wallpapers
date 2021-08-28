package wallgram.hd.wallpapers.ui.details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.http.Url
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.util.downloadx.download

import java.net.URL
import javax.inject.Inject

class DownloadViewModel @Inject constructor(): BaseViewModel(){


        private var disposable: Disposable? = null

        fun download(url: String){

        }

}