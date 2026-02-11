package org.six.series.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  <T> ComboBox(
    items: List<T>,
    label:String="",
    current: T?,
    itemLabel: (T) -> String,
    onSelect: (T) -> Unit,
    editable: Boolean=false,
) {
    var selected by remember { mutableStateOf<T?>(current) }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,

        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = current?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                if(editable)
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )
        if (editable) {
            ExposedDropdownMenu(
                expanded = editable && expanded,
                onDismissRequest = { expanded = false },
                //modifier = Modifier.exposedDropdownSize()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(item)) },
                        onClick = {
                            selected = item
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
