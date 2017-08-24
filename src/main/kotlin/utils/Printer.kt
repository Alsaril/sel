package utils

object Printer {
    fun printBarcode(barcode: String) {
        val pj = java.awt.print.PrinterJob.getPrinterJob()
        pj.setPrintable({ graphics, _, pageIndex ->
            run {
                if (pageIndex > 0) {
                    java.awt.print.Printable.NO_SUCH_PAGE
                } else {
                    val bean = org.krysalis.barcode4j.impl.code128.Code128Bean()
                    bean.doQuietZone(false);
                    val g2d = graphics as java.awt.Graphics2D
                    g2d.translate(300, 100)
                    g2d.scale(5.0, 5.0)

                    val canvas = org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider(g2d, 0)
                    bean.generateBarcode(canvas, barcode)

                    java.awt.print.Printable.PAGE_EXISTS
                }
            }
        })
        pj.print()
    }


    val HEADER_TEMPLATE = "templates/header.html"
    val ROW_TEMPLATE = "templates/row.html"
    val FOOTER_TEMPLATE = "templates/footer.html"

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
        val headerTemplate = String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(utils.Printer.HEADER_TEMPLATE)))
        val rowTemplate = String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(utils.Printer.ROW_TEMPLATE)))
        val footerTemplate = String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(utils.Printer.FOOTER_TEMPLATE)))

        val headerLength = utils.Printer.NAME.length

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

        javafx.embed.swing.JFXPanel()
        javafx.application.Platform.runLater({
            val stage = javafx.stage.Stage()
            val wv = javafx.scene.web.WebView()
            wv.engine.loadContent(html)
            stage.scene = javafx.scene.Scene(wv)
            stage.setOnShown {
                val pj = javafx.print.PrinterJob.createPrinterJob()
                val b = pj.printPage(wv)
                if (b) {
                    pj.endJob()
                }
                javafx.application.Platform.runLater({ stage.close() })
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