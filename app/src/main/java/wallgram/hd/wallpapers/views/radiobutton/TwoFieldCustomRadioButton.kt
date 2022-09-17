package wallgram.hd.wallpapers.views.radiobutton

import android.content.Context;
import android.util.AttributeSet;
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView;
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.presentation.SkuMapper
import wallgram.hd.wallpapers.presentation.subscribe.Subscription


class TwoFieldCustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle), LifecycleOwner, SkuMapper {

    protected val lifecycleRegistry = LifecycleRegistry(this)

    private var priceTextView: TextView
    private var periodTextView: TextView
    private var discountTextView: TextView

    init {
        View.inflate(context, R.layout.custom_button_two_field, this)

        priceTextView = findViewById(R.id.price_text)
        periodTextView = findViewById(R.id.period_text)
        discountTextView = findViewById(R.id.discount_text)

        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CustomRadioButtonOneField)
            if (a.hasValue(R.styleable.CustomRadioButtonOneField_price_field))
                setPrice(a.getString(R.styleable.CustomRadioButtonOneField_price_field)!!)

            if (a.hasValue(R.styleable.CustomRadioButtonOneField_period_field))
                setPeriod(a.getString(R.styleable.CustomRadioButtonOneField_period_field)!!)

            if (a.hasValue(R.styleable.CustomRadioButtonOneField_discount_field))
                setDiscount(a.getString(R.styleable.CustomRadioButtonOneField_discount_field)!!)


            a.recycle()
        }
    }

    private fun setPrice(price: String) {
        priceTextView.text = price
    }

    private fun setPeriod(price: String) {
        periodTextView.text = price
    }

    private fun setDiscount(price: String) {
        discountTextView.text = price
    }

    override fun map(data: Subscription) {
        data.price().observe(this){
            setPrice(it)
        }
        data.isPurchased().observe(this){
            setPurchased(it)
        }
    }

    private fun setPurchased(isPurchased: Boolean){
        discountTextView.visibility = if (isPurchased) VISIBLE else GONE
        //currentTextView.visibility = if (isPurchased) VISIBLE else GONE
    }

    override fun getLifecycle() = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    fun getPrice() = priceTextView.text.toString()

    fun getDuration(): String = when (id) {
        R.id.month_sub -> {
            "1" + context.getString(R.string.month).replace("/", "")
        }
        R.id.year_sub -> {
            "1" + context.getString(R.string.year).replace("/", "")
        }
        else -> "0"
    }



//    override fun getPrice(): String? {
//        return price
//    }
//
//    override val duration: String
//        get() = if (id == R.id.month_sub) {
//            "1" + context.getString(R.string.month).replace("/", "")
//        } else if (id == R.id.year_sub) {
//            "1" + context.getString(R.string.year).replace("/", "")
//        } else "0"
//
//    override fun populateViews() {
//        priceTextView!!.text = price
//        periodTextView!!.text = period
//        discountTextView!!.text = discount
//    }
}
