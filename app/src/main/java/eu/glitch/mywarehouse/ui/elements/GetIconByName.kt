package eu.glitch.mywarehouse.ui.elements

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource


@Composable
fun GetIconByName(modifier: Modifier = Modifier, name: String, contentDescription: String) {
    val context = LocalContext.current
    val drawableId = context.resources.getIdentifier(
        name,
        "drawable",
        context.packageName
    )
    Icon(
        modifier = modifier,
        imageVector = ImageVector.vectorResource(drawableId),
        contentDescription = contentDescription
    )
}