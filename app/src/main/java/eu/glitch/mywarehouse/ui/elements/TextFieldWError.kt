package eu.glitch.mywarehouse.ui.elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldWError(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    error: String?,
    keyboardNumber: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        label = {
            Text(
                text = label
            )
        },
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error!!)
            }
        },
        keyboardOptions = if (keyboardNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        trailingIcon = trailingIcon ?: trailingIcon
    )
}