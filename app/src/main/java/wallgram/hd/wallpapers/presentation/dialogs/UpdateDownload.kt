package wallgram.hd.wallpapers.presentation.dialogs

import androidx.work.WorkInfo
import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface UpdateDownload {

    interface Observe : Communication.Observe<WorkInfo>
    interface Update : Communication.Update<WorkInfo>

    class Base @Inject constructor() : Communication.UiUpdate<WorkInfo>(), Observe, Update
}