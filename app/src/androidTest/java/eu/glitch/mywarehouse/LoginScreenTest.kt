package eu.glitch.mywarehouse

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.glitch.mywarehouse.database.impl.AccountServiceImpl
import eu.glitch.mywarehouse.database.impl.FirestoreRepositoryImpl
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.navigation.Destination
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavGraph
import eu.glitch.mywarehouse.navigation.NavigationRouterImpl
import eu.glitch.mywarehouse.ui.screens.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SettingsScreenTest {

    private lateinit var navRouter: INavigationRouter

    private lateinit var settingsViewModel: SettingsViewModel


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        navRouter = NavigationRouterImpl(NavController(composeRule.activity))
        hiltRule.inject()
    }

    @Test
    fun see_login() {
        launchSettingsScreen()

        with(composeRule) {
            onNodeWithTag("email_text_field").assertExists()
            onNodeWithTag("email_text_field").assertIsDisplayed()
            onNodeWithTag("email_text_field").assert(hasText(""))

            onNodeWithTag("password_text_field").assertExists()
            onNodeWithTag("password_text_field").assertIsDisplayed()
            onNodeWithTag("password_text_field").assert(hasText(""))

            onNodeWithTag("login_register_button").assertExists()
            onNodeWithTag("login_register_button").isDisplayed()
            onNodeWithTag("login_register_button").assertIsEnabled()

            onNodeWithTag("toggle_login_register").assertExists()
            onNodeWithTag("toggle_login_register").isDisplayed()
            onNodeWithTag("toggle_login_register").assertIsEnabled()
        }
    }

    //@Test
    //fun check_login_functionality() {
    //    launchSettingsScreen()
//
    //    with(composeRule) {
    //        onNodeWithTag("email_text_field").performTextInput("user@example.com")
    //        onNodeWithTag("password_text_field").performTextInput("password123")
//
    //        onNodeWithTag("login_register_button").performClick()
//
    //        onNodeWithTag("logged_in_as").assertExists()
//
    //        onNodeWithTag("sync_button").assertExists()
    //        onNodeWithTag("sync_button").assertExists()
//
    //        onNodeWithTag("invite_user_section").assertExists()
    //    }
    //}
//
    //@Test
    //fun check_invite_user_section() {
    //    launchSettingsScreen()
//
    //    with(composeRule) {
    //        onNodeWithTag("invite_email_field").performTextInput("newuser@example.com")
//
    //        onNodeWithTag("role_selector").performClick()
    //        onNodeWithTag("DropdownMenuItem").assertExists().performClick()
//
    //        onNodeWithTag("invite_button").assertExists()
//
    //        onNodeWithTag("invite_button").performClick()
//
    //        onNodeWithTag("invite_error_message").assertDoesNotExist()
    //    }
    //}

    @Test
    fun check_logout_functionality() {
        launchSettingsScreen()

        with(composeRule) {
            onNodeWithTag("logout_button").performClick()

            onNodeWithTag("email_text_field").assertExists()

            onNodeWithTag("password_text_field").assertExists()
        }
    }

    //@Test
    //fun check_leave_company_functionality() {
    //    launchSettingsScreen()
//
    //    with(composeRule) {
    //        onNodeWithTag("leave_company_button").performClick()
//
    //        onNodeWithTag("leave_company_button").assertExists()
    //    }
    //}



    private fun launchSettingsScreen() {
        composeRule.activity.setContent {
            MaterialTheme {
                NavGraph(startDestination = Destination.SettingsScreen.route)
            }
        }
    }
}
