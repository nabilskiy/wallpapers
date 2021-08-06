package wallgram.hd.wallpapers.util.download.manager

interface TaskLimitation {
    fun start(taskManager: TaskManager)

    fun stop(taskManager: TaskManager)

    fun delete(taskManager: TaskManager)
}