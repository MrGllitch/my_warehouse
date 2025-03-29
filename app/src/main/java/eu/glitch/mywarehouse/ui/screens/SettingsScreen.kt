package eu.glitch.mywarehouse.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
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

    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    toastMessage?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
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
    paddingValues: PaddingValues,
    actions: SettingsActions,
    data: SettingsData
) {
    Column(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(16.dp)
    ) {
        if (data.user == null) {
            var isLogin by remember { mutableStateOf(true) }
            Spacer(modifier = Modifier.weight(0.8f))

            // Email Text Field
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

            // Password Text Field
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


            // Error Message
            if (data.error != null) {
                Text(text = data.error!!, color = androidx.compose.ui.graphics.Color.Red)
            }

            Spacer(modifier = Modifier.weight(0.05f))

            // Toggle between Login/Register
            Text(
                text = if (!isLogin) "Already have an account? Click here to login" else "Don't have an account yet? Click here to register",
                modifier = Modifier
                    .clickable { isLogin = !isLogin }
                    .padding(bottom = 8.dp),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.weight(0.05f))

            // Login/Register Button
            Button(
                onClick = { if (isLogin) actions.login() else actions.register() },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(text = if (isLogin) "Login" else "Register")
            }
            Spacer(modifier = Modifier.weight(1f))
        } else {
            // Logged-in state
            Text(
                text = "Logged in as ${data.user!!.email}",
                modifier = Modifier.fillMaxWidth(),
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )

            // Sync Button
            Button(
                onClick = { actions.syncData() },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(text = data.syncStatus ?: "Sync Database")
            }

            if (data.user!!.companyId == null) {
                // Invite User Section
                Text(text = "Invite User", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = data.inviteEmail,
                        onValueChange = { actions.inviteEmailChanged(it) },
                        modifier = Modifier.weight(3f),
                        placeholder = { Text(text = "user@email.com") },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    RoleSelector(
                        roles = listOf("admin", "employee"),
                        selectedRole = data.inviteRole,
                        onRoleSelected = { actions.inviteRoleChanged(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Invite Error Handling
                if (data.inviteEmailError != null) {
                    Text(text = data.inviteEmailError!!, color = androidx.compose.ui.graphics.Color.Red)
                }

                // Invite Button
                Button(
                    onClick = { actions.invite() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Invite")
                }

                // Users List
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    data.users?.forEach { user ->
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = user.email, modifier = Modifier.weight(1f))
                                Button(
                                    onClick = { actions.removeEmployee(user.email) },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(text = "Remove")
                                }
                            }
                        }
                    }
                }

                // Invitations List
                if (data.invitations?.isEmpty() == true) {
                    Text(text = "No invitations", modifier = Modifier.padding(top = 16.dp))
                } else {
                    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                        data.invitations?.forEach {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "${it.senderEmail} - ${it.role}", modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = { actions.acceptInvite(it.id) },
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(text = "Accept")
                                    }
                                    Button(
                                        onClick = { actions.removeInvite(it.id) },
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(text = "Remove")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Under Company
                Text(text = "You are under: ${data.senderEmail}")
                Button(
                    onClick = { actions.leaveCompany() },
                    modifier = Modifier.padding(top = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Leave Company")
                }
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

