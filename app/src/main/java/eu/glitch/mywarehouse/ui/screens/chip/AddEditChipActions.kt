package eu.glitch.mywarehouse.ui.screens.chip

import eu.glitch.mywarehouse.model.Chip

interface AddEditChipActions {
    fun nameChanged(text: String)
    fun addEditChip(): Boolean
    fun deleteChip()
    fun isAddEditOpen(state: Boolean)
    fun editChipChanged(chip: Chip)

}