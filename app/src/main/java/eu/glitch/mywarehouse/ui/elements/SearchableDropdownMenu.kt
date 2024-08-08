package eu.glitch.mywarehouse.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SearchableDropdownMenu(
    items: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.contains(searchText, ignoreCase = true) }
    var selectedOption =
        remember { filteredItems.firstOrNull() } // Default selection based on filtered items

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )

    IconButton(onClick = { expanded = !expanded }) {
        // Display currently selected option (if any)
        Text(selectedOption ?: "Select")
        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Menu")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        if (filteredItems.isEmpty()) {
            Text("No results found")
        } else {
            filteredItems.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        selectedOption = option
                    }
                )
            }
        }
    }
}