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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import eu.glitch.mywarehouse.R

@Composable
fun AddEditRow(
    name: String?,
    id: Long?,
    onAddEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var isAlertVisible by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        if (id == null) {
            Spacer(modifier = Modifier.weight(1.0f))
            Button(onClick = { onAddEdit() }) {
                Text(text = stringResource(R.string.add))
            }
        } else {
            Spacer(modifier = Modifier.weight(1.0f))
            Button(onClick = { isAlertVisible = true }) {
                Text(text = stringResource(R.string.remove))
            }
            Button(onClick = { onAddEdit() }) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
    if (isAlertVisible) {
        AlertDialog(
            onDismissRequest = { isAlertVisible = false },
            title = {
                Text(buildAnnotatedString {
                    append(stringResource(R.string.are_u_sure))
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
                    append(stringResource(R.string.will_be_deleted))
                })
            },
            confirmButton = {
                Button(
                    onClick = { onDelete() }) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { isAlertVisible = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}