package utils

import java.text.ParseException
import java.text.SimpleDateFormat

object Utils {
    private val backendDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private val normalDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

    fun formatDate(date: String): String? {
        return try {
            normalDateFormat.format(backendDateFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun print() {
        //Printer.INSTANCE.printCheck(1,"4545454", new Date().toString(),25,"25:25,");
    }

    fun fieldCheck(s: String): String {
        if (s != "") {
            return s
        } else {
            return "нет данных"
        }
    }

    fun dataFormat(s: String): String {
        if (s == "нет данных") {
            return ""
        } else {
            return s
        }
    }

}

typealias CloseListener<T> = (result: T) -> Unit

enum class Measure(val str: String) {
    SH("шт"), M("м"), KG("кг"), M2("м²"), M3("м³");

    companion object {
        private val map = HashMap<String, Measure>()

        init {
            Measure.values().forEach { map[it.str] = it }
        }

        fun fromName(str: String) = map[str]
        fun items(): List<Measure> = Measure.values().asList()
    }
    override fun toString() = str
}