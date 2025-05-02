package eu.glitch.mywarehouse

import eu.glitch.mywarehouse.database.impl.AccountServiceImpl
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.database.impl.FirestoreRepositoryImpl
import eu.glitch.mywarehouse.database.impl.SyncStatus
import eu.glitch.mywarehouse.model.User
import eu.glitch.mywarehouse.ui.screens.Invite
import eu.glitch.mywarehouse.ui.screens.SettingsViewModel
import eu.glitch.mywarehouse.ui.screens.SettingsUIState
import eu.glitch.mywarehouse.ui.screens.SettingsData
import io.mockk.Runs
import io.mockk.andThenJust
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    private val accountService: AccountServiceImpl = mockk()
    private val repository: RepositoryImpl = mockk()
    private val firestore: FirestoreRepositoryImpl = mockk()

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(accountService, repository, firestore)

        Dispatchers.setMain(UnconfinedTestDispatcher())

        coEvery { firestore.updateDaoForNewCompany() } returns Unit
        coEvery { accountService.authenticate(any(), any()) } returns AccountServiceImpl.AuthResult.Success("Success")
        coEvery { accountService.fetchEmployees(any()) } returns listOf(User(id = "mockUid", email = "employee@example.com"))
        coEvery { accountService.fetchInvites() } returns listOf(Invite(id = "mockInviteId", senderEmail = "sender@example.com", receiverEmail = "receiver@example.com", role = "employee", status = "pending"))
        every { accountService.currentUser } returns User(id = "mockUid", email = "user@example.com")
        every { accountService.logout() } returns Unit andThenJust Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test inviteEmailChanged updates state`() = runTest {
        val newEmail = "new@example.com"
        val initialState = viewModel.settingsUIState.value

        viewModel.inviteEmailChanged(newEmail)

        val updatedState = viewModel.settingsUIState.value
        assertNotEquals(initialState, updatedState)
        assertTrue(updatedState is SettingsUIState.SettingStateChanged)
        assertEquals(newEmail, (updatedState as SettingsUIState.SettingStateChanged).data.inviteEmail)
    }

    @Test
    fun `test syncData updates sync status`() = runTest {
        coEvery { repository.syncDataToCloud() } returns flowOf(SyncStatus.Syncing)

        viewModel.syncData()

        assertEquals(SyncStatus.Syncing, viewModel.syncStatus.value)
    }


    @Test
    fun `test invite sends error for empty email`() = runTest {
        val initialState = viewModel.settingsUIState.value
        viewModel.data.inviteEmail = ""

        viewModel.invite()

        val updatedState = viewModel.settingsUIState.value
        assertNotEquals(initialState, updatedState)
        assertTrue(updatedState is SettingsUIState.SettingStateChanged)
        assertEquals("Email is required", (updatedState as SettingsUIState.SettingStateChanged).data.inviteEmailError)
    }

    @Test
    fun `test login sets user data successfully`() = runTest {
        val email = "user@example.com"
        val password = "password"
        val mockUser = User(id = "mockUid", email = email)

        coEvery { accountService.authenticate(any(), any()) } returns AccountServiceImpl.AuthResult.Success(mockUser.email)

        every { accountService.currentUser } returns mockUser

        viewModel.emailChanged(email)
        viewModel.passwordChanged(password)
        viewModel.login()

        assertNotNull(viewModel.data.user)
        assertEquals(mockUser.email, viewModel.data.user!!.email)
    }
    @Test
    fun `test inviteRoleChanged updates state`() = runTest {
        val newRole = "admin"
        val initialState = viewModel.settingsUIState.value

        viewModel.inviteRoleChanged(newRole)

        val updatedState = viewModel.settingsUIState.value
        assertNotEquals(initialState, updatedState)
        assertTrue(updatedState is SettingsUIState.SettingStateChanged)
        assertEquals(newRole, (updatedState as SettingsUIState.SettingStateChanged).data.inviteRole)
    }

    @Test
    fun `test acceptInvite updates toast message on success`() = runTest {
        val mockInviteId = "mockInviteId"
        val mockUser = User(id = "mockUid", email = "user@example.com")
        viewModel.data.user = mockUser

        coEvery { accountService.acceptInvitation(any(), any()) } returns AccountServiceImpl.AuthResult.Success("Invitation accepted successfully")

        viewModel.acceptInvite(mockInviteId)

        assertEquals("Invitation accepted successfully", viewModel.toastMessage.value)
    }

    @Test
    fun `test removeInvite updates toast message`() = runTest {
        val mockInviteId = "mockInviteId"
        coEvery { accountService.removeInvitation(any()) } returns Unit

        viewModel.removeInvite(mockInviteId)

        assertEquals("Invitation removed", viewModel.toastMessage.value)
    }

    @Test
    fun `test leaveCompany updates toast message on success`() = runTest {
        coEvery { accountService.leaveCompany() } returns Unit

        viewModel.leaveCompany()

        assertEquals("Successfully left", viewModel.toastMessage.value)
    }

    @Test
    fun `test removeEmployee updates toast message on success`() = runTest {
        val mockEmployeeEmail = "employee@example.com"
        coEvery { accountService.removeEmployee(any()) } returns Unit

        viewModel.removeEmployee(mockEmployeeEmail)

        assertEquals("Employee removed", viewModel.toastMessage.value)
    }

}
