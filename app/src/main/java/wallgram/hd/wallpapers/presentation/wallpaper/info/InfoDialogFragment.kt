package wallgram.hd.wallpapers.presentation.wallpaper.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.databinding.FragmentInfoDialogBinding
import wallgram.hd.wallpapers.presentation.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.linktextview.Linker
import wallgram.hd.wallpapers.util.withArgs

@AndroidEntryPoint
class InfoDialogFragment : BaseFragment<InfoViewModel, FragmentInfoDialogBinding>(
    FragmentInfoDialogBinding::inflate
) {

    private val pic: Int by args(ARG_ID, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val infoAdapter = InfoAdapter()

        binding.infoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = infoAdapter
        }

        viewModel.pic.observe(viewLifecycleOwner) {
            it.map(infoAdapter)
        }

        viewModel.fetch(pic)
    }

    override val viewModel: InfoViewModel by viewModels()

    companion object {
        const val TAG = "InfoDialogFragment"

        private const val ARG_ID = "arg_id"
        fun create(id: Int) = InfoDialogFragment().withArgs(
            ARG_ID to id
        )
    }
}