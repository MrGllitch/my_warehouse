package eu.glitch.mywarehouse.ui.elements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.glitch.mywarehouse.R


@Composable
fun IconPicker(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
    iconRoute: String,
    displayed: Boolean
) {
    data class IconData(val imageVector: ImageVector, val name: String, val route: String)

    var selectedIconRoute by remember {
        mutableStateOf(iconRoute)
    }


    val icons = mutableListOf<IconData>(
    )

    val fieldNames = R.drawable::class.java.fields
    fieldNames.forEach { field ->
        val route = field.name
        if (route.startsWith("icon_")) {
            val name = route.substringAfter("icon_").replace("_", " ")
            val id = field.getInt(null)
            icons.add(IconData(ImageVector.vectorResource(id), name, route))
        }
    }



    if (displayed) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier.background(color = Color.White)) {


                HorizontalDivider()
                LazyVerticalGrid(
                    modifier = Modifier
                        .heightIn(320.dp, 320.dp)
                        .padding(4.dp, 8.dp),
                    columns = GridCells.Fixed(4)
                ) {
                    icons.forEach {
                        item {
                            IconButton(

                                modifier = Modifier
                                    .clip(RoundedCornerShape(44))
                                    .background(color = if (it.route == selectedIconRoute) Color.LightGray else Color.Transparent),
                                onClick = { selectedIconRoute = it.route }) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = it.imageVector,
                                    contentDescription = "",


                                    )
                            }
                        }
                    }


                }

                HorizontalDivider()
                Row {
                    OutlinedButton(onClick = {
                        selectedIconRoute = iconRoute
                        onSelect(selectedIconRoute)
                        onDismiss()
                    },
                        modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        onSelect(selectedIconRoute)
                        onDismiss()
                    },
                        modifier = Modifier.padding(end = 16.dp)) {
                        Text(text = "Select")
                    }
                }
            }
        }
    }


}


