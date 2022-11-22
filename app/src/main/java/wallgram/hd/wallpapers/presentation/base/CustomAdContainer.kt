package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isNotEmpty
import com.google.android.gms.ads.nativead.NativeAdView
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class CustomAdContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MyView {

    override fun showAd(banner: RecyclerBannerAd) {
        val adview = banner.adView()
        val unifiedAdBinding = adview.second
        val nativeAdView = adview.second.root
        val nativeAd = adview.first.forNativeAd { nativeAd ->
            nativeAdView.mediaView = unifiedAdBinding.adMedia

            // Set other ad assets.
            nativeAdView.headlineView = unifiedAdBinding.adHeadline
            nativeAdView.bodyView = unifiedAdBinding.adBody
            nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
            nativeAdView.iconView = unifiedAdBinding.adAppIcon
            nativeAdView.priceView = unifiedAdBinding.adPrice
            nativeAdView.starRatingView = unifiedAdBinding.adStars
            nativeAdView.storeView = unifiedAdBinding.adStore
            nativeAdView.advertiserView = unifiedAdBinding.adAdvertiser

            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            unifiedAdBinding.adHeadline.text = nativeAd.headline
            nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.setMediaContent(it) }

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                unifiedAdBinding.adBody.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adBody.visibility = View.VISIBLE
                unifiedAdBinding.adBody.text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adCallToAction.visibility = View.VISIBLE
                unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                unifiedAdBinding.adAppIcon.visibility = View.GONE
            } else {
                unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
                unifiedAdBinding.adAppIcon.visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                unifiedAdBinding.adPrice.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adPrice.visibility = View.VISIBLE
                unifiedAdBinding.adPrice.text = nativeAd.price
            }

            if (nativeAd.store == null) {
                unifiedAdBinding.adStore.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adStore.visibility = View.VISIBLE
                unifiedAdBinding.adStore.text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                unifiedAdBinding.adStars.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adStars.rating = nativeAd.starRating!!.toFloat()
                unifiedAdBinding.adStars.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                unifiedAdBinding.adAdvertiser.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adAdvertiser.text = nativeAd.advertiser
                unifiedAdBinding.adAdvertiser.visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            nativeAdView.setNativeAd(nativeAd)
        }

        if(isNotEmpty())
            removeAllViews()


        addView(unifiedAdBinding.root)


        banner.load(nativeAd)


    }


}

