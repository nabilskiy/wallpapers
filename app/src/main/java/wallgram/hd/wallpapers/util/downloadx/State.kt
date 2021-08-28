package wallgram.hd.wallpapers.util.downloadx

sealed class State {
    var progress: Progress = Progress()
        internal set

    class None : State()
    class Waiting : State()
    class Downloading : State()
    class Stopped : State()
    class Failed : State()
    class Succeed : State()
}