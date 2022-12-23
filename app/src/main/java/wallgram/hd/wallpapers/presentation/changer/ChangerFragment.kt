package wallgram.hd.wallpapers.presentation.changer

import androidx.fragment.app.viewModels
import wallgram.hd.wallpapers.databinding.FragmentChangerBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.feed.FeedViewModel

class ChangerFragment : BaseFragment<ChangerViewModel, FragmentChangerBinding>(
    FragmentChangerBinding::inflate
) {
    override val viewModel: ChangerViewModel by viewModels()
}