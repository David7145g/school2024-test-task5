import java.awt.BorderLayout
import java.io.File
import javax.swing.JFrame
import javax.swing.SwingUtilities

const val basePath = "src/main/resources/"
const val interestFilePath = basePath + "interest.txt"
const val influenceFilePath = basePath + "influence.txt"
const val resultFilePath = basePath + "result.txt"

data class Stakeholder(var name: String, var interest: Double, var influence: Double)

fun main() {
    val stakeholders = mutableListOf<Stakeholder>()

    fun processFile(filePath: String, setter: (Stakeholder, Double) -> Unit) {

        // чтение строк из файла
        val lines = File(filePath).readLines()

        // получение имен стейкхолдеров
        val names = lines[0].split('|').map { it.trim() }

        // создание матрицы попарного сравнения
        val matrix = lines.drop(1).map { line ->

            line.split(' ').map {
                it.toDoubleOrNull() ?: 0.0
            }
        }

        for (i in matrix.indices) {

            // считаем сумму влияния/интереса
            val sums = matrix[i].sum()

            // берем/создаем стейкхолдера
            val stakeholder = stakeholders.find { it.name == names[i] }
                ?: Stakeholder(names[i], 0.0, 0.0).also { stakeholders.add(it) }

            // устанавливаем значение
            setter(stakeholder, sums)
        }
    }

    processFile(
        filePath = interestFilePath,
        setter = { sh, value -> sh.interest = value }
    )

    processFile(
        filePath = influenceFilePath,
        setter = { sh, value -> sh.influence = value }
    )

    // выбираем имена значимых стейкхолдеров
    val importantStakeholdersName = stakeholders
        .filter { it.interest >= 2.5 && it.influence >= 2.5 }
        .map { it.name }

    // записываем имена значимых стейкхолдеров в файл
    File(resultFilePath).writeText(importantStakeholdersName.joinToString("\n"))

    // Так же можно посмотреть импровизируемый график
    SwingUtilities.invokeLater {
        val chart = Chart()
        val frame = JFrame("Chart")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.add(chart, BorderLayout.CENTER)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true

        stakeholders.forEach {
            chart.addPoint(it.name, it.influence, it.interest)
        }
    }
}