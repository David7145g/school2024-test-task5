import java.awt.*
import java.awt.geom.Ellipse2D
import javax.swing.JPanel

class Chart : JPanel() {
    private val points = mutableListOf<Triple<String, Double, Double>>()

    fun addPoint(name: String, x: Double, y: Double) {
        points.add(Triple(name, x, y))
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val graphics2D = g as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics2D.color = Color.BLACK

        val margin = 100
        val width = bounds.width - 2 * margin
        val height = bounds.height - 2 * margin

        with(graphics2D) {

            // Ось X
            drawLine(margin, height + margin, margin + width, height + margin)
            // Ось Y
            drawLine(margin, margin, margin, margin + height)

            // Далее поделим пополам каждую из осей
            val dash1 = floatArrayOf(10f)
            val stroke = BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash1, 0f)
            graphics2D.stroke = stroke

            drawLine(margin, (height / 2) + margin, margin + width, (height / 2) + margin)
            drawLine(margin + (width / 2), margin, margin  + (width / 2), margin + height)
        }

        val xScale = width / 5
        val yScale = height / 5

        for ((name, x, y) in points) {
            val xPos = margin + (x * xScale).toInt()
            val yPos = margin + height - (y * yScale).toInt()

            graphics2D.fill(Ellipse2D.Double(xPos - 5.0, yPos - 5.0, 10.0, 10.0))
            graphics2D.color = Color.BLACK
            graphics2D.drawString(name, xPos - 10, yPos - 10)
        }
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(640, 480)
    }
}
