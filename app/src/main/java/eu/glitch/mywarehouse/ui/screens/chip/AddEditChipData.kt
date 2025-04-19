package eu.glitch.mywarehouse.ui.screens.chip

import eu.glitch.mywarehouse.model.Chip

class AddEditChipData {
    var chips: MutableList<Chip> = mutableListOf()
    var chip = Chip("", "")
    var isAddEditOpen: Boolean = false
    var isAdmin = true

    var nameError: String? = null

}