package wallgram.hd.wallpapers.core

import android.util.Log

class LoggingHashMap<K, V>: LinkedHashMap<K, V>() {

    override fun put(key: K, value: V): V? {
        Log.i("LoggingHashMap", "put:$key")
        return super.put(key, value)
    }

    override fun remove(key: K): V? {
        Log.i("LoggingHashMap", "remove:$key")
        return super.remove(key)
    }

}