package wallgram.hd.wallpapers.util.download.storage

import wallgram.hd.wallpapers.util.download.task.Task

interface Storage {
    fun load(task: Task)

    fun save(task: Task)

    fun delete(task: Task)
}