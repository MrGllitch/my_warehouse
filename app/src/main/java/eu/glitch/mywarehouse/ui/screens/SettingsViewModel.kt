package eu.glitch.mywarehouse.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.glitch.mywarehouse.database.impl.AccountServiceImpl
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.database.impl.SyncStatus
import eu.glitch.mywarehouse.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val account: AccountServiceImpl,
    private val repository: RepositoryImpl
) : ViewModel(), SettingsActions {
    var data: SettingsData = SettingsData()

    private val _settingsUIState: MutableStateFlow<SettingsUIState> =
        MutableStateFlow(value = SettingsUIState.Loading)
    val settingsUIState = _settingsUIState.asStateFlow()

    private val _syncStatus: MutableStateFlow<SyncStatus> = MutableStateFlow(value=SyncStatus.Waiting)
    val syncStatus =  _syncStatus.asStateFlow()

    override fun syncData() {
        viewModelScope.launch {
            repository.syncDataToCloud().collect { status ->
                _syncStatus.update { status }
            }
        }
    }

    fun loadUser(){
        viewModelScope.launch {
            if (account.currentUser.id != ""){
                data.user = account.currentUser
            }else{
                data.user = null
            }
            _settingsUIState.update {
                SettingsUIState.SettingStateChanged(data)
            }
        }
    }

    override fun emailChanged(new: String) {
        data.email = new
        _settingsUIState.update {
            SettingsUIState.SettingStateChanged(data)
        }
    }

    override fun passwordChanged(new: String) {
        data.password = new
        _settingsUIState.update {
            SettingsUIState.SettingStateChanged(data)
        }
    }

    override fun login() {
        viewModelScope.launch {
            if(!account.authenticate(data.email,data.password)) {
            data.invalidCredentials="Invalid credentials"
            }else{
                data.invalidCredentials = null
            }

            loadUser()
        }
    }

    override fun logout() {
        account.logout()
        loadUser()
    }

}

sealed class SettingsUIState {
    object Loading : SettingsUIState()
    class SettingStateChanged(val data: SettingsData) : SettingsUIState()
}

class SettingsData {
    var isProductItemList: Boolean? = null
    var user: User? = null
    var email = ""
    var password = ""
    var emailError: String? = null
    var passwordError: String? = null
    var invalidCredentials: String? = null
    var syncStatus: String? = null
}

interface SettingsActions {
    fun emailChanged(new: String)
    fun passwordChanged(new: String)
    fun login()
    fun logout()
    fun syncData()


}