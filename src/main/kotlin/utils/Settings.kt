package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.sync.Mutex
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock

class Setting<T>(private val default: T) {
    internal var value: T = default

    fun setValue(value: T) {
        this.value = value
    }

    fun stringValue() = value as String
    fun intValue() = value as Int
    fun doubleValue() = value as Double
}

class Settings private constructor(val filename: String) {

    private val settings = hashMapOf<String, Setting<Any>>()
    private val _lock = ReentrantReadWriteLock()
    private val readLock = _lock.readLock()
    private val writeLock = _lock.writeLock()

    fun hasName(name: String) = settings.containsKey(name)

    operator fun get(name: String) = lock(readLock) { settings[name]!! }

    /*
        sets setting[name] = value if value != null and typeof(value) == typeof(setting.value)
     */
    operator fun set(name: String, value: Any?) = lock(writeLock) {
        if (value == null) return@lock
        settings[name]?.let {
            if (it.value::class.java != value::class.java) return@lock
            it.setValue(value)
            softSave()
        }
    }

    private val mutex = Mutex()

    private fun softSave() = launch(CommonPool) {
        mutex.lock()
        val clear = hashMapOf<String, Any?>()
        settings.forEach {
            clear[it.key] = it.value.value
        }
        val raw = gson.toJson(clear)
        try {
            Files.write(Paths.get(filename), raw.toByteArray())
        } finally {
            mutex.unlock()
        }
    }

    fun forceSave() = runBlocking {
        softSave().join()
    }

    private fun load() {
        try {
            val raw = String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8)
            val clear = gson.fromJson<Map<String, Any>>(raw, type)
            lock(writeLock) {
                for ((name, setting) in settings) {
                    set(name, clear[name])
                }
            }
        } catch (e: Exception) {
        }
    }

    infix fun String.to(that: Any) {
        settings[this] = Setting(that)
    }

    companion object {
        private val gson = Gson()
        private val type = object : TypeToken<Map<String, Map<String, String>>>() {}.getType()

        fun create(filename: String, init: Settings.() -> Unit): Settings {
            val settings = Settings(filename)
            settings.init()
            settings.load()
            return settings
        }
    }
}

fun <T> lock(lock: Lock, block: () -> T): T {
    lock.lock()
    try {
        return block()
    } finally {
        lock.unlock()
    }
}
/*

    val settings = create("cfg.txt") {
        "token" to ""
        "username" to "user1"
        "inn" to "234234234234"
    }

 */