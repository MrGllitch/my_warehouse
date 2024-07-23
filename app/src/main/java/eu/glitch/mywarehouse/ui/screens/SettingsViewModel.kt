package eu.glitch.mywarehouse.ui.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {
    var data: SettingsData = SettingsData()

    private val _settingsUIState: MutableStateFlow<SettingsUIState> =
        MutableStateFlow(value = SettingsUIState.Loading)
    val settingsUIState = _settingsUIState.asStateFlow()

}

sealed class SettingsUIState {
    object Loading : SettingsUIState()
    class SettingStateChanged(val data: SettingsData) : SettingsUIState()
}

class SettingsData {
    var isProductItemList: Boolean? = null
}