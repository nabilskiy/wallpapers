package wallgram.hd.wallpapers.views.radiobutton

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import wallgram.hd.wallpapers.views.radiobutton.OnCustomRadioButtonListener
import wallgram.hd.wallpapers.views.radiobutton.CustomRadioGroup
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import wallgram.hd.wallpapers.views.radiobutton.BaseCustomRadioButton
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.subscribe.Subscription
import wallgram.hd.wallpapers.presentation.subscribe.SubscriptionUi


class CustomRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    fun setOnClickListener(onClickListener: OnCustomRadioButtonListener?) {
        Companion.onClickListener = onClickListener
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        child.setOnClickListener {
            val selectedButton = child as OneFieldCustomRadioButton
            setAllButtonsToUnselectedState()
            setSelectedButtonToSelectedState(selectedButton)
            initOnClickListener(selectedButton)
        }
        super.addView(child, index, params)
    }

    fun setSelectedButton(button: OneFieldCustomRadioButton) {
        setAllButtonsToUnselectedState()
        setSelectedButtonToSelectedState(button)
    }

    fun setCurrent(button: BaseCustomRadioButton) {
        val container: LinearLayout = this
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is BaseCustomRadioButton) {
//                BaseCustomRadioButton containerView = (BaseCustomRadioButton) child;
//                containerView.setCurrent(containerView == button);
            }
        }
    }

    private fun setAllButtonsToUnselectedState() =
        children.forEach {
            if (it is OneFieldCustomRadioButton)
                setButtonToUnselectedState(it)
        }


    val selectedButton: Int
        get() {
            var id = -1
            for (i in 0 until this.childCount) {
                val child = getChildAt(i)
                if (child is OneFieldCustomRadioButton) {
                    if (child.isSelected()) {
                        id = child.getId()
                        return id
                    }
                }
            }
            return id
        }

    fun subscription(): Subscription {
        var subscription: Subscription = Subscription.Empty()

        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (child is OneFieldCustomRadioButton) {
                if (child.isSelected()) {
                    subscription = child.subscription()
                    return subscription
                }
            }
        }

        return subscription
    }

    private fun setButtonToUnselectedState(button: OneFieldCustomRadioButton) {
        button.isSelected = false
        button.check(false)
    }

    fun setSelectedButtonToSelectedState(button: OneFieldCustomRadioButton) {
        button.isSelected = true
        button.check(true)
    }

    private fun initOnClickListener(selectedButton: OneFieldCustomRadioButton) {
        onClickListener?.onClick(selectedButton.subscription())
    }

    companion object {
        private var onClickListener: OnCustomRadioButtonListener? = null
    }
}
