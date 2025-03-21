package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.database.impl.AccountServiceImpl.AuthResult
import eu.glitch.mywarehouse.model.User
import eu.glitch.mywarehouse.ui.screens.Invite


interface IAccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: User?

    suspend fun authenticate(email: String, password: String) : AuthResult

    suspend fun register(email: String, password: String) : AuthResult

    suspend fun sendInvitation(senderEmail: String, receiverEmail: String, role: String): AuthResult

    suspend fun acceptInvitation(invitationId: String, userId: String): AuthResult

    suspend fun removeInvitation(invitationId: String)

    suspend fun fetchInvites(): List<Invite>

    suspend fun fetchEmployees(companyId: String): List<User>

    suspend fun removeEmployee(email: String)

    suspend fun leaveCompany()

     fun logout()

     enum class Role {
         ADMIN,
         EMPLOYEE
     }
}