package eu.glitch.mywarehouse.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun EditAction(modifier: Modifier) {
    Box(modifier = modifier.background(Color.Yellow), contentAlignment = Alignment.Center) {
        Text(text = "Edit")
    }
}