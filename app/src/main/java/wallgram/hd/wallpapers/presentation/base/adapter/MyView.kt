package wallgram.hd.wallpapers.presentation.base.adapter

import android.view.View
import androidx.annotation.DrawableRes
import com.google.android.gms.ads.AdView
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd
import wallgram.hd.wallpapers.presentation.subscribe.Subscription

interface MyView {

    fun show(text: CharSequence) = Unit

    fun show(textId: Int) = Unit

    fun setGradientDrawable(startColor: Int, endColor: Int) = Unit

    fun <T : ItemUi> show(data: List<T>, carouselAdapter: GenericAdapter<*>) = Unit

    fun showAd(banner: RecyclerBannerAd) = Unit

    fun subscriptions(data: List<Subscription>) = Unit

    fun textColor(color: Int) = Unit

    fun textSize(size: Float) = Unit

    fun loadImage(url: String) = Unit

    fun loadImage(preview: String, original: String) = Unit

    fun showImageResource(@DrawableRes id: Int) = Unit

    fun showStartImageResource(@DrawableRes id: Int) = Unit

    fun textAlignment(alignment: Int) = Unit

    fun enable(enabled: Boolean) = Unit

    fun check(checked: Boolean) = Unit

    fun handleClick(listener: View.OnClickListener) = Unit
}