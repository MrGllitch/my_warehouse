package eu.glitch.mywarehouse.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.glitch.mywarehouse.R
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
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Profile") },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },

        ) {
            SettingsScreenContent(
                paddingValues = it, actions = viewModel, data = data
            )
        }
    }
    )
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    actions: SettingsActions,
    data: SettingsData
) {
    Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
        if (data.user == null){
            TextFieldWError(label = "email", value = data.email, onValueChanged = {
                actions.emailChanged(
                    it
                )
            }, error = data.emailError)
            TextFieldWError(
                label = "password",
                value = data.password,
                onValueChanged = { actions.passwordChanged(it) },
                error = data.passwordError
            )
            if (data.invalidCredentials != null){
                Text(text = data.invalidCredentials!!)
            }
            Button(onClick = { actions.login() }) {
                Text(text = "Login")
            }
        }else{

                Text(text = "Logged in as ${data.user!!.email}")
                Text(text = "Premium: ${data.user!!.hasPremium}")

            Button(onClick = { actions.logout() }) {
                Text(text = "log out")
            }
        }
    }

}