package eu.glitch.mywarehouse.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

interface FilterItem {
    val id: Long?
    val name: String
}

@Composable
fun <E : FilterItem> FilterRow(
    modifier: Modifier = Modifier,
    items: List<E>,
    type: String,
    onClick: (String, Long, Boolean) -> Unit

) {
    LazyRow(modifier = modifier) {
        items.forEach {
            item {
                Filter(
                    id = it.id!!,
                    name = it.name,
                    type = type,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun Filter(
    id: Long,
    type: String,
    name: String,
    onClick: (String, Long, Boolean) -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        onClick = {
            selected = !selected
            onClick(type, id, selected)
        },
        label = {
            Text(name)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}