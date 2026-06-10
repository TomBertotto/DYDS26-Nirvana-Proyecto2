package edu.dyds.recipes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edu.dyds.recipes.presentation.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Recipes App",
    ) {
        App()
    }
}

