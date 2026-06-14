package edu.dyds.countries

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edu.dyds.countries.presentation.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Countries App",
    ) {
        App()
    }
}
