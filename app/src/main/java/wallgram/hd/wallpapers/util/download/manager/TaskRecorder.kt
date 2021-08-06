package wallgram.hd.wallpapers.util.download.manager

import wallgram.hd.wallpapers.util.download.task.Task

interface TaskRecorder {
    fun insert(task: Task)

    fun update(task: Task, status: Status)

    fun delete(task: Task)
}