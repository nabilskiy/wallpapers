package wallgram.hd.wallpapers.util.download.watcher

import wallgram.hd.wallpapers.util.download.task.Task

interface Watcher {
    fun watch(task: Task)

    fun unwatch(task: Task)
}