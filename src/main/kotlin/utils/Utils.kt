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
}

typealias CloseListener<T> = (result: T) -> Unit