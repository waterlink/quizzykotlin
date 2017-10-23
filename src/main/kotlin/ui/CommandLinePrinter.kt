package ui

open class CommandLinePrinter {

    open fun println(line: String) {
        // has to use fully-qualified name to avoid the name clash
        kotlin.io.println(line)
    }

}