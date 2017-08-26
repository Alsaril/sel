package start

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import models.Product
import java.io.IOException

class Main : Application() {

    var primaryStage: Stage? = null
        private set
    private var rootLayout: VBox? = null

    val productsData = FXCollections.observableArrayList<Product>()

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        this.primaryStage!!.title = "Accounting"

        initRootLayout()
        showProductsOverview()
    }

    fun initRootLayout() {
        try {
            val loader = FXMLLoader()
            loader.location = Main::class.java.getResource("/view/RootLayout.fxml")
            rootLayout = loader.load<Any>() as VBox
            val scene = Scene(rootLayout!!)
            primaryStage!!.scene = scene
            primaryStage!!.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun showProductsOverview() {
        try {

            val loader = FXMLLoader()
            loader.location = Main::class.java.getResource("/view/main/MainView.fxml")
            val mainView = loader.load<Any>() as AnchorPane
            rootLayout!!.children.add(mainView)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}