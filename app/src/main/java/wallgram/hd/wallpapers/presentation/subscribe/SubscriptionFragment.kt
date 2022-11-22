package wallgram.hd.wallpapers.presentation.subscribe

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.*
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.databinding.FragmentSubscriptionBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.subscribe.adapter.SubscriptionAdapter
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.views.radiobutton.OnCustomRadioButtonListener
import wallgram.hd.wallpapers.views.radiobutton.OneFieldCustomRadioButton

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<SubscriptionViewModel, FragmentSubscriptionBinding>(
    FragmentSubscriptionBinding::inflate
) {

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    private var currentSub = DEFAULT_SKU

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.openPlayStoreSubscriptionsEvent.observe(viewLifecycleOwner) { sku ->
            openPlayStoreSubscriptions(sku)
        }

        val subscriptionAdapter = SubscriptionAdapter()

        with(binding) {
            backBtn.setOnClickListener {
                modo.back()
            }

            contentView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = subscriptionAdapter
            }

//            subscribeBtn.setOnClickListener {
//                val sub = when (radioGroup.selectedButton) {
//                    R.id.month_sub -> MONTH_SKU
//                    R.id.year_sub -> YEAR_SKU
//                    else -> currentSub
//                }
//
//                viewModel.buySku(requireActivity(), sub, currentSub)
//            }
//
//            radioGroup.setOnClickListener(object : OnCustomRadioButtonListener {
//                override fun onClick(view: View) {
//                    val v = when (view.id) {
//                        R.id.year_sub -> yearSub
//                        else -> monthSub
//                    }
//
//                    setSubText(v)
//                }
//            })
        }

        viewModel.fetch(viewLifecycleOwner) {
            it.map(subscriptionAdapter)
        }

        viewModel.buySubscription(viewLifecycleOwner) {
            viewModel.buySku(requireActivity(), it)
        }

        viewModel.updateSubscriptions(viewLifecycleOwner) {
            viewModel.fetch(viewLifecycleOwner) {
                it.map(subscriptionAdapter)
            }
        }


    }

//    private fun setSubText(view: OneFieldCustomRadioButton) {
//        binding.textSub.text = getString(
//            R.string.sub_text,
//            view.getDuration(),
//            view.getPrice()
//        )
//    }

    private fun openPlayStoreSubscriptions(sku: String) {
        if (sku == DEFAULT_SKU)
            return
        val url =
            String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL, sku, requireActivity().packageName)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override val viewModel: SubscriptionViewModel by viewModels()

}