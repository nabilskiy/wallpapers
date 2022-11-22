package wallgram.hd.wallpapers.presentation.subscribe

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemSubscriptionContentBinding
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.subscribe.buy.BuySubscriptions
import wallgram.hd.wallpapers.views.radiobutton.OnCustomRadioButtonListener
import javax.inject.Inject

@AndroidEntryPoint
class SubscriptionContentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), MyView {

    private val binding: ItemSubscriptionContentBinding

    private val subs: MutableList<Subscription> = mutableListOf()

    @Inject
    lateinit var buySubscriptions: BuySubscriptions.Update

    init {
        binding = ItemSubscriptionContentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun subscriptions(data: List<Subscription>) {
        subs.clear()
        subs.addAll(data)

        init(data)
    }

    fun init(data: List<Subscription>) = with(binding) {
        binding.subscribeBtn.setOnClickListener {
            buySubscriptions.map(radioGroup.subscription().map(Subscription.Mapper.Id()))
        }
        radioGroup.setOnClickListener(object : OnCustomRadioButtonListener {
            override fun onClick(subscription: Subscription) {
                updateDescription(subscription)
            }

        })

        if (data.size >= 2) {
            binding.monthSub.map(data[0])
            binding.yearSub.map(data[1])

            if (data.none { it.map(Subscription.Mapper.Purchased()) }) {
                binding.radioGroup.setSelectedButton(binding.yearSub)
                updateDescription(data.get(1))
            }
        }


    }


    private fun updateDescription(subscription: Subscription) {
        val mapped = subscription.map(Subscription.Mapper.Description())
        val period =
            if (mapped.first == "1month") context.getString(R.string.month) else context.getString(R.string.year)

        binding.textSub.text = context.getString(
            R.string.sub_text,
            period,
            mapped.second
        )
    }

}