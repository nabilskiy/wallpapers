package wallgram.hd.wallpapers.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

fun singleIoThread(block: () -> Unit) {
    singleIO.execute(block)
}

fun ioThread(block: () -> Unit) {
    diskIO.execute(block)
}

fun mainThread(block: () -> Unit) {
    mainHandler.post(block)
}

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

fun ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        mainThread {
            block()
        }
    }
}

fun assertMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        throw IllegalStateException("This operation only supports the Main thread call!")
    }
}

fun <T> assertMainThreadWithResult(block: () -> T): T {
    if (isMainThread()) {
        return block()
    } else {
        throw IllegalStateException("This operation only supports the Main thread call!")
    }
}

private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

private val singleIO by lazy {
    Executors.newSingleThreadExecutor(object : ThreadFactory {
        private val threadId = AtomicInteger(0)
        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r)
            thread.name = "iron_branch_single_io_${threadId.getAndIncrement()}"
            thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
                Log.w("IronBranch", "${t.name} -> ${e.message}", e)
            }
            return thread
        }
    })
}

private val diskIO by lazy {
    Executors.newFixedThreadPool(4, object : ThreadFactory {
        private val threadId = AtomicInteger(0)
        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r)
            thread.name = "iron_branch_io_${threadId.getAndIncrement()}"
            thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
                Log.w("IronBranch", "${t.name} -> ${e.message}", e)
            }
            return thread
        }
    })
}