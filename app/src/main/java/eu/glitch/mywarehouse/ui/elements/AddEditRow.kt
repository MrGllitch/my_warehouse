package eu.glitch.mywarehouse.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun AddEditRow(
    name: String?,
    id: Long?,
    onAddEdit: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    var isAlertVisible by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        if (id == null) {
            Button(onClick = { onCancel() }) {
                Text(text = "Zrusit")
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Button(onClick = { onAddEdit() }) {
                Text(text = "Pridat")
            }
        } else {
            Button(onClick = { onCancel() }) {
                Text(text = "Zrusit")
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Button(onClick = { isAlertVisible = true }) {
                Text(text = "Smazat")
            }
            Button(onClick = { onAddEdit() }) {
                Text(text = "Zmenit")
            }
        }
    }
    if (isAlertVisible) {
        AlertDialog(
            onDismissRequest = { isAlertVisible = false },
            title = {
                Text(buildAnnotatedString {
                    append("Are you sure you want to delete ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$name")
                    }
                    append("?")
                })

            },
            text = {
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$name ")
                    }
                    append("will be permanently deleted from your app and you cant access it anymore in all page of applications!")
                })
            },
            confirmButton = {
                Button(
                    onClick = { onDelete() }) {
                    Text(text = "yes")
                }
            },
            dismissButton = {
                Button(onClick = { isAlertVisible = false }) {
                    Text(text = "cancel")
                }
            }
        )
    }
}