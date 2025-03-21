package eu.glitch.mywarehouse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.database.impl.SyncStatus
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.TextFieldWError
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<SettingsViewModel>()

    var data by remember {
        mutableStateOf(SettingsData())
    }


    val syncState = viewModel.syncStatus.collectAsStateWithLifecycle()
    syncState.value.let { status ->
        data.syncStatus = when (status) {
            is SyncStatus.Syncing -> {
                "Syncing..."
            }

            is SyncStatus.TableSyncing -> {
                "Syncing ${status.tableName}..."
            }

            is SyncStatus.Success -> {
                "Synced"
            }

            is SyncStatus.Error -> {
                status.message
            }

            SyncStatus.Waiting -> {
                "Sync Database"
            }
        }
    }


    val state = viewModel.settingsUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {

            SettingsUIState.Loading -> {
                viewModel.loadUser()
            }

            is SettingsUIState.SettingStateChanged -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Profile") }, navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                },
                    actions = {
                        Button(onClick = { viewModel.logout() }) {
                            Text(text = "Logout")
                        }
                    })

            },

            ) {
            SettingsScreenContent(
                paddingValues = it, actions = viewModel, data = data
            )
        }
    })
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues, actions: SettingsActions, data: SettingsData
) {
    Column(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(16.dp),
    ) {
        if (data.user == null) {
            var isLogin by remember { mutableStateOf(true)}
            Spacer(modifier = Modifier.weight(0.8f))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                value = data.email,
                onValueChange = {
                    actions.emailChanged(
                        it
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.weight(0.01f))
            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                value = data.password,
                onValueChange = { actions.passwordChanged(it) },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.icon_visibility
                    else R.drawable.icon_visibility_off

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon( painter = painterResource(image), description)
                    }
                }
                )
            Spacer(modifier = Modifier.weight(0.05f))
            if (data.error != null) {
                Text(text = data.error!!)
            }
            Spacer(modifier = Modifier.weight(0.05f))
            Text(text = if (!isLogin) "Already have account? Click here to login" else "Dont have account yet? Click here to register", modifier = Modifier.clickable { isLogin = !isLogin })
            Spacer(modifier = Modifier.weight(0.05f))
            Button(onClick = { if (isLogin) actions.login() else actions.register() }, modifier = Modifier.fillMaxWidth()) {
                if (isLogin) {
                    Text(text = "Login")
                } else {
                    Text(text = "Register")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        } else {

            Text(text = "Logged in as ${data.user!!.email}", modifier = Modifier.fillMaxWidth())
            Button(onClick = { actions.syncData() }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (data.syncStatus == null) {
                        "sync"
                    } else {
                        data.syncStatus!!
                    }
                )
            }

            if (data.user!!.companyId == null){
                Text(text = "Invite User")
            Row {
                OutlinedTextField(placeholder = { Text(text = "user@email.com")},value = data.inviteEmail, onValueChange = { actions.inviteEmailChanged(it) }, modifier = Modifier.weight(3f))
                RoleSelector(roles = listOf("admin", "employee"), selectedRole = data.inviteRole, onRoleSelected = { actions.inviteRoleChanged(it) }, modifier = Modifier.weight(1f))

            }
                if (data.inviteEmailError != null){
                    Text(text = data.inviteEmailError!!)
                }
                Button(onClick = { actions.invite() }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Invite")
                }
            LazyColumn {
                data.users?.forEach { 
                    item { 
                        Row {
                            Text(text = it.email)
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = { actions.removeEmployee(it.email) }) {
                                Text(text = "Remove")
                            }
                        }
                    }
                }
            }
                if (data.users?.size == 0 ){
                    Text(text = "Invitations")
                    LazyColumn {
                        data.invitations?.forEach{
                            item {
                                Row {
                                    Text(text = "${it.receiverEmail} - ${it.role}")
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(onClick = { actions.acceptInvite(it.id) }) {
                                        Text(text = "Accept")
                                    }
                                    Button(onClick = { actions.removeInvite(it.id) }) {
                                        Text(text = "Remove")
                                    }
                                }
                            }
                        }
                    }
                }

        }else{
        //todo accept / quit
        }
    }
}
}

@Composable
fun RoleSelector(
    modifier: Modifier = Modifier,
    roles: List<String>,
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier

    ) {

        OutlinedButton(
            onClick = { expanded = !expanded },
        ) {
            Text(text = selectedRole.ifEmpty { "Select Role" })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(
                    text = { Text(text = role) },
                    onClick = {
                        onRoleSelected(role)
                        expanded = false
                    }
                )
            }
        }
    }
}

