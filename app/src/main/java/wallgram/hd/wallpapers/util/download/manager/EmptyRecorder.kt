package wallgram.hd.wallpapers.util.download.manager

import wallgram.hd.wallpapers.util.download.task.Task

object EmptyRecorder : TaskRecorder {

    override fun insert(task: Task) {
    }

    override fun update(task: Task, status: Status) {
    }

    override fun delete(task: Task) {
    }
}