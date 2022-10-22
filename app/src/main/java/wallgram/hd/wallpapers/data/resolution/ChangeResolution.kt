package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.presentation.settings.resolution.UpdateResolutions

interface ChangeResolution {

    fun changeResolution(data: String)

    class Combo(
        private val changeResolution: ChangeResolution,
        private val communication: UpdateResolutions.Update
    ) : ChangeResolution {
        override fun changeResolution(data: String) {
            changeResolution.changeResolution(data)
            communication.map(true)
        }

    }

}