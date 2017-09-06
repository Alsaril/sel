package utils

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import models.Node
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
    fun cashboxDate() = cashboxDateFormat.format(Date())
    fun cashboxTime() = cashboxTimeFormat.format(Date())

    val noData = "Нет данных"

    fun fieldCheck(s: String): String = if (s != "") s else noData

    fun dataFormat(s: String): String = if (s == noData) "" else s

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

class TreeNodeHelper(val node: Node) : Comparable<TreeNodeHelper> {
    private val childs = mutableListOf<TreeNodeHelper>()

    fun addChild(node: TreeNodeHelper) = childs.add(node)

    fun childs(): MutableList<TreeNodeHelper> {
        Collections.sort(childs)
        return childs
    }

    override fun compareTo(other: TreeNodeHelper) = node.compareTo(other.node)
}

fun createSortedTree(nodes: List<Node>): List<TreeItem<Node>> {
    val items = HashMap<Int, TreeNodeHelper>()
    nodes.forEach {
        items[it.id] = TreeNodeHelper(it)
    }

    val roots = mutableListOf<TreeNodeHelper>()

    items.values.forEach {
        val parent = it.node.parent
        if (parent == null) {
            roots.add(it)
        } else {
            items[parent]?.addChild(it)
        }
    }

    fun buildTree(node: TreeNodeHelper): TreeItem<Node> {
        val item = TreeItem(node.node)
        node.childs().forEach { item.children.add(buildTree(it)) }
        return item
    }

    return roots.map { buildTree(it) }
}

fun Double.twoPoints() = String.format("%.2f", this)
fun Double.noPoints() = String.format("%.0f", this)

class PasswordDialog : Dialog<String>() {
    private val passwordField: PasswordField

    init {
        title = "Пароль"
        headerText = "Введите пароль"

        val ok = ButtonType("ОК", ButtonData.OK_DONE)
        val cancel = ButtonType("Отмена", ButtonData.CANCEL_CLOSE)
        dialogPane.buttonTypes.addAll(ok, cancel)

        passwordField = PasswordField()
        passwordField.promptText = "Пароль"

        val hBox = HBox()
        hBox.children.add(passwordField)
        hBox.padding = Insets(20.0)

        HBox.setHgrow(passwordField, Priority.ALWAYS)

        dialogPane.content = hBox

        Platform.runLater { passwordField.requestFocus() }

        setResultConverter { dialogButton ->
            if (dialogButton === ok) {
                return@setResultConverter passwordField.text
            }
            null
        }
    }
}