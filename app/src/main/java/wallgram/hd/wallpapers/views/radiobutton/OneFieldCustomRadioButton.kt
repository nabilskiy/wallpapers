package wallgram.hd.wallpapers.views.radiobutton

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.SkuMapper
import wallgram.hd.wallpapers.presentation.subscribe.Subscription


class OneFieldCustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), SkuMapper {

    private var radioButton: AppCompatRadioButton
    private var priceTextView: TextView
    private var periodTextView: TextView

    private var sub: Subscription = Subscription.Empty()

    init {
        View.inflate(context, R.layout.custom_button_one_field, this)

        priceTextView = findViewById(R.id.price_text)
        periodTextView = findViewById(R.id.period_text)
        radioButton = findViewById(R.id.radio_button)

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

    }

    override fun map(data: Subscription) {
        sub = data
        data.map(Subscription.Mapper.Ui()).apply {
            setPrice(first)
            setPurchased(second)
        }
    }

    fun subscription(): Subscription = sub


    private fun setPurchased(isPurchased: Boolean) {
        check(isPurchased)
    }

    fun check(isChecked: Boolean) {
        radioButton.isChecked = isChecked
    }

    fun isChecked() = radioButton.isChecked

    fun getPrice() = priceTextView.text.toString()

    fun getDuration(): String = when (id) {
        R.id.month_sub -> {
            context.getString(R.string.month)
        }
        R.id.year_sub -> {
            context.getString(R.string.year)
        }
        else -> "0"
    }

}
