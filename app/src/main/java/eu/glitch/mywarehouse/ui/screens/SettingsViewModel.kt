package eu.glitch.mywarehouse.ui.screens

import android.util.Log
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

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

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
            // Check if the invite email is empty
            if (data.inviteEmail == "") {
                data.inviteEmailError = "Email is required"
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
                return@launch
            } else {
                data.inviteEmailError = null
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
            }

            // Check if the user is trying to invite themselves
            if (data.inviteEmail == data.user!!.email) {
                data.inviteEmailError = "You cannot invite yourself"
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
                return@launch
            }

            val invitationExists = account.isInvitationExisting(data.inviteEmail)
            if (invitationExists) {
                // If the user already has an invitation
                data.inviteEmailError = "This user has already been invited"
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
                return@launch
            }

            try {
                account.sendInvitation(data.user!!.email, data.inviteEmail, data.inviteRole)
                _toastMessage.update { "Invitation sent" }
                data.inviteEmail = ""
            } catch (e: Exception) {
                data.inviteEmailError = "Error sending invitation"
                _settingsUIState.update {
                    SettingsUIState.SettingStateChanged(data)
                }
            }
        }
    }



    override fun acceptInvite(invitationId: String) {
        viewModelScope.launch {
            when (val result = account.acceptInvitation(invitationId, data.user!!.id)) {
                is AccountServiceImpl.AuthResult.Success -> {
                    _toastMessage.update { "Invitation accepted successfully" }
                }
                is AccountServiceImpl.AuthResult.Failure -> {
                    _toastMessage.update { "Failed to accept invitation: ${result.error}" }
                }
                is AccountServiceImpl.AuthResult.Exception -> {
                    _toastMessage.update { "Error: ${result.exception.localizedMessage}" }
                }
            }
            loadUser()
        }
    }

    override fun removeInvite(invitationId: String) {
        viewModelScope.launch {
            account.removeInvitation(invitationId)
            _toastMessage.update { "Invitation removed" }
            loadUser()
        }
    }

    override fun leaveCompany() {
        viewModelScope.launch {
            account.leaveCompany()
            loadUser()
            _toastMessage.update { "Successfully left" }
        }
    }

    override fun removeEmployee(email: String) {
        viewModelScope.launch {
            account.removeEmployee(email)
            _toastMessage.update { "Employee removed" }
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
            }else if(data.user != null){
                data.acceptedInvitations = account.fetchAcceptedInvitesForUser(data.user!!.email)
                data.senderEmail = data.acceptedInvitations!!.firstOrNull()?.senderEmail
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
    fun clearToastMessage() {
        _toastMessage.update { null }
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
    var acceptedInvitations: List<Invite>? = null
    var senderEmail: String? = null

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