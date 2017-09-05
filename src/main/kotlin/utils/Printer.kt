package utils

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.PrinterJob
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Stage
import org.krysalis.barcode4j.impl.code128.Code128Bean
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider
import java.awt.print.Printable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors


object Printer {
    fun printBarcode(barcode: String) {
        val pj = java.awt.print.PrinterJob.getPrinterJob()
        pj.setPrintable({ graphics, _, pageIndex ->
            run {
                if (pageIndex > 0) {
                    java.awt.print.Printable.NO_SUCH_PAGE
                } else {
                    val bean = Code128Bean()
                    bean.doQuietZone(false);
                    val g2d = graphics as java.awt.Graphics2D
                    g2d.translate(30, 30)
                    g2d.scale(5.0, 5.0)

                    val canvas = Java2DCanvasProvider(g2d, 0)
                    bean.generateBarcode(canvas, barcode)

                    Printable.PAGE_EXISTS
                }
            }
        })
        pj.print()
    }


    val HEADER_TEMPLATE = "/templates/header.html"
    val ROW_TEMPLATE = "/templates/row.html"
    val FOOTER_TEMPLATE = "/templates/footer.html"

    fun loadString(path: String) = BufferedReader(InputStreamReader(
            Printer::class.java.getResource(path).openStream(), "utf-8")).lines().collect(Collectors.joining("\n"));


    val NAME = "Человек-продавец"

    fun printCheck(cashbox: Int,
                   inn: String,
                   date: String,
                   number: Int,
                   time: String,
                   positions: List<utils.Position>,
                   sum: String,
                   cash: String,
                   odd: String,
                   operator: String) {
        val headerTemplate = loadString(HEADER_TEMPLATE)
        val rowTemplate = loadString(ROW_TEMPLATE)
        val footerTemplate = loadString(FOOTER_TEMPLATE)

        val headerLength = NAME.length

        val headerBuilder = StringBuilder()
        for (i in 1..headerLength + 2) {
            headerBuilder.append('*')
        }

        val asteriskHeader = headerBuilder.toString()

        val header = headerTemplate.format(asteriskHeader, "*${utils.Printer.NAME}*", asteriskHeader, cashbox, inn, date, number, time)

        val rows: String = positions.stream()
                .map { (vendor, name, price, count, sum) -> rowTemplate.format(vendor, name, price, count, sum) }
                .collect(java.util.stream.Collectors.joining(""))

        val footer = footerTemplate.format(sum, cash, odd, operator)

        val html = "$header$rows$footer"

        JFXPanel()
        Platform.runLater({
            val stage = Stage()
            val wv = WebView()
            wv.engine.loadContent(html)
            stage.scene = Scene(wv)
            stage.setOnShown {
                val printer = javafx.print.Printer.getDefaultPrinter()
                val pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, javafx.print.Printer.MarginType.HARDWARE_MINIMUM)
                val pj = PrinterJob.createPrinterJob()
                val b = pj.printPage(pageLayout, wv)
                if (b) pj.endJob()
                Platform.runLater({ stage.close() })
            }
            stage.show()
        })
    }
}

data class Position(
        val vendor: String,
        val name: String,
        val price: String,
        val count: String,
        val sum: String
)