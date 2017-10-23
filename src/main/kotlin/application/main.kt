package application

import ui.CommandLinePrinter
import ui.CommandLineUser

fun main(args: Array<String>) {
    val application = CommandLineApplication(
            args = args,
            commandLineUser = CommandLineUser(),
            commandLinePrinter = CommandLinePrinter(),
            quizStorage = FileSystemQuizStorage())

    application.run()
}