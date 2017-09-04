package utils

import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableView
import javafx.scene.control.TreeView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val backendDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private val normalDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    private val cashboxTimeFormat = SimpleDateFormat("HH:mm")
    private val cashboxDateFormat = SimpleDateFormat("dd.MM.yyyy")

    fun formatDate(date: String): String? {
        return try {
            normalDateFormat.format(backendDateFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun print() {
        //Printer.printCheck(1,"4545454", Date().toString(),25,"25:25,");
    }

    fun cashboxDate() = cashboxDateFormat.format(Date())
    fun cashboxTime() = cashboxTimeFormat.format(Date())

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

fun parseDouble(str: String) = str.replace(',', '.').toDoubleOrNull()

/*

    val contextMenu = makeMenu {
        "abc" to ::editProduct
        "dew" to ::delProduct
    }

 */

class TableMenu<out T>(private val table: TableView<T>) {
    val menu = ContextMenu()

    infix fun String.to(action: (T) -> Any) {
        val menuItem = MenuItem(this)
        menuItem.onAction = EventHandler {
            action(table.selectionModel.selectedItem)
        }
        menu.items.add(menuItem)
    }
}

fun <T> makeMenu(table: TableView<T>, create: TableMenu<T>.() -> Unit) {
    val menu = TableMenu(table)
    menu.create()
    table.contextMenu = menu.menu
}

class TreeMenu<out T>(private val table: TreeView<T>) {
    val menu = ContextMenu()

    infix fun String.to(action: (T) -> Any) {
        val menuItem = MenuItem(this)
        menuItem.onAction = EventHandler {
            action(table.selectionModel.selectedItem.value)
        }
        menu.items.add(menuItem)
    }
}

fun <T> makeMenu(tree: TreeView<T>, create: TreeMenu<T>.() -> Unit) {
    val menu = TreeMenu(tree)
    menu.create()
    tree.contextMenu = menu.menu
}