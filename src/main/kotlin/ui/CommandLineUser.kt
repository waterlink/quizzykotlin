package ui

open class CommandLineUser {

    open fun readCommand(): String {
        return readLine()!!.trim()
    }

}