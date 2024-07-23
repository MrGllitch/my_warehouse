package eu.glitch.mywarehouse.ui.screens.chip

import eu.glitch.mywarehouse.model.Chip

class AddEditChipData {
    var chips: MutableList<Chip> = mutableListOf()
    var chip = Chip(null, "")
    var isAddEditOpen: Boolean = false

    var nameError: String? = null

}