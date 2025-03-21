package eu.glitch.mywarehouse.ui.screens

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

    override fun inviteEmailChanged(new: String) {
        data.inviteEmail = new
        _settingsUIState.update {
            SettingsUIState.SettingStateChanged(data)
        }
    }

    override fun inviteRoleChanged(new: String) {
        data.inviteRole = new
        _settingsUIState.update {
            SettingsUIState.SettingStateChanged(data)
        }
    }

    override fun invite() {
        viewModelScope.launch {
            if (data.inviteEmail == ""){
                data.inviteEmailError = "Email is required"
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
                return@launch
            }else{
                data.inviteEmailError = null
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
            }
        account.sendInvitation(data.user!!.email, data.inviteEmail, data.inviteRole)
        }
    }

    override fun acceptInvite(invitationId: String) {
        viewModelScope.launch {
            account.acceptInvitation(invitationId, data.user!!.id)
        }
    }

    override fun removeInvite(invitationId: String) {
        viewModelScope.launch {
            account.removeInvitation(invitationId)
        }
    }

    override fun leaveCompany() {
        viewModelScope.launch {
            account.leaveCompany()
        }
    }

    override fun removeEmployee(email: String) {
        viewModelScope.launch {
            account.removeEmployee(email)
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            if (account.currentUser != null) {
                data.user = account.currentUser
            } else {
                data.user = null
            }

            if (data.user?.companyId == null && data.user != null) {
                data.users = account.fetchEmployees(data.user!!.id)
                data.invitations = account.fetchInvites()
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
            when (val result = account.authenticate(data.email, data.password)) {
                is AccountServiceImpl.AuthResult.Success -> {
                    data.error = null
                }
                is AccountServiceImpl.AuthResult.Failure -> {
                    data.error = result.error
                }
                is AccountServiceImpl.AuthResult.Exception -> {
                    data.error = result.exception.localizedMessage ?: "Unknown error occurred"
                }
            }
            loadUser()
        }
    }

    override fun register() {
        viewModelScope.launch {
            when (val result = account.register(data.email, data.password)) {
                is AccountServiceImpl.AuthResult.Success -> {
                    data.error = null
                }
                is AccountServiceImpl.AuthResult.Failure -> {
                    data.error = result.error
                }
                is AccountServiceImpl.AuthResult.Exception -> {
                    data.error = result.exception.localizedMessage ?: "Unknown error occurred"
                }
            }
            loadUser()
        }
    }

    override fun logout() {
        account.logout()
        data.user = null
        data.email = ""
        data.password = ""
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
    var error: String? = null
    var syncStatus: String? = null

    var inviteEmail = ""
    var inviteEmailError: String? = null
    var inviteRole = "employee"

    var users: List<User>? = null
    var invitations: List<Invite>? = null

}

class Invite {
    var id = ""
    var senderId = ""
    var senderEmail = ""
    var receiverEmail = ""
    var role = ""
    var status = ""
}

interface SettingsActions {
    fun emailChanged(new: String)
    fun passwordChanged(new: String)
    fun login()
    fun register()
    fun logout()
    fun syncData()

    fun inviteEmailChanged(new: String)
    fun inviteRoleChanged(new: String)
    fun invite()
    fun acceptInvite(invitationId: String)
    fun removeInvite(invitationId: String)
    fun leaveCompany()
    fun removeEmployee(email: String)


}