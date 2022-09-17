package wallgram.hd.wallpapers.util.blurhash

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions


// FOR GLIDE
fun RequestBuilder<Drawable>.blurPlaceHolder(
    blurString: String,
    width: Int = 0,
    height: Int = 0,
    blurHash: BlurHash,
    response: (requestBuilder: RequestBuilder<Drawable>) -> Unit
) {
    if (width != 0 && height != 0) {
        blurHash.execute(blurString, width, height) { drawable ->
            this@blurPlaceHolder.placeholder(drawable)
            response(this@blurPlaceHolder)
        }
    }
}

fun RequestBuilder<Drawable>.blurPlaceHolder(
    blurString: String,
    targetView: View,
    blurHash: BlurHash,
    response: (requestBuilder: RequestBuilder<Drawable>) -> Unit
) {
    targetView.post {
        blurPlaceHolder(blurString, targetView.width, targetView.height, blurHash, response)
    }
}

fun RequestOptions.blurPlaceHolderOf(
    blurString: String,
    width: Int = 0,
    height: Int = 0,
    blurHash: BlurHash,
    response: (requestOptions: RequestOptions) -> Unit
) {
    if (width != 0 && height != 0) {
        blurHash.execute(blurString, width, height) { drawable ->
            this@blurPlaceHolderOf.placeholder(drawable)
            response(this@blurPlaceHolderOf)
        }
    }
}

fun RequestOptions.blurPlaceHolderOf(
    blurString: String,
    targetView: View,
    blurHash: BlurHash,
    response: (requestOptions: RequestOptions) -> Unit
) {
    targetView.post {
        blurPlaceHolderOf(blurString, targetView.width, targetView.height, blurHash, response)
    }
}