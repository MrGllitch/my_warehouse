package eu.glitch.mywarehouse.database.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import eu.glitch.mywarehouse.database.IAccountService
import eu.glitch.mywarehouse.model.User
import eu.glitch.mywarehouse.ui.screens.Invite
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) :
    IAccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null
    override val currentUser: User?
        get() = runBlocking {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                try {
                    val userDocRef = firestore.collection("users").document(firebaseUser.uid)
                    val documentSnapshot = userDocRef.get().await()

                    if (documentSnapshot.exists()) {
                        return@runBlocking documentSnapshot.toObject(User::class.java)
                    } else {
                        // Create a new User if no document exists
                        val newUser = User(
                            id = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            companyId = null,
                            role = null
                        )
                        userDocRef.set(newUser).await()
                        return@runBlocking newUser
                    }
                } catch (e: Exception) {
                    throw IllegalStateException("Failed to fetch user data from Firestore", e)
                }
            } else {
                // Return null if the user is not logged in
                return@runBlocking null
            }
        }



    override suspend fun authenticate(email: String, password: String): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (continuation.isActive) {
                            continuation.resume(AuthResult.Success("Authentication successful"))
                        }
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error occurred"
                        if (continuation.isActive) {
                            continuation.resume(AuthResult.Failure(errorMessage))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resume(AuthResult.Exception(exception))
                    }
                }
        }
    }


    override suspend fun register(email: String, password: String): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (continuation.isActive) {
                            continuation.resume(AuthResult.Success("Registration successful"))
                        }
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error occurred"
                        if (continuation.isActive) {
                            continuation.resume(AuthResult.Failure(errorMessage))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resume(AuthResult.Exception(exception))
                    }
                }
        }
    }

    override suspend fun sendInvitation(
        senderEmail: String,
        receiverEmail: String,
        role: String
    ): AuthResult {
        val invitationRef = firestore.collection("invitations").document()

        val invitation = mapOf(
            "senderEmail" to senderEmail,
            "receiverEmail" to receiverEmail,
            "status" to "pending",
            "role" to role
        )
        invitationRef.set(invitation).await()
        return AuthResult.Success("Invitation sent successfully")
    }

    override suspend fun acceptInvitation(
        invitationId: String,
        userId: String,
    ): AuthResult {
        try {
            val invitationRef = firestore.collection("invitations").document(invitationId)
            val document = invitationRef.get().await()

            if (document.exists()) {
                val invitation = document.toObject(Invite::class.java)

                if (invitation != null) {
                    return if (invitation.status == "pending") {
                        val userRef = firestore.collection("users").document(userId)
                        userRef.update(
                            "companyId", invitation.senderId,
                            "role", invitation.role
                        ).await()

                        val companyRef = firestore.collection("users").document(invitation.senderId)
                        companyRef.collection("employees").document(userId).set(
                            mapOf("role" to invitation.role, "userId" to userId, "email" to invitation.receiverEmail)
                        ).await()

                        invitationRef.update("status", "accepted").await()

                        return AuthResult.Success("Invitation accepted successfully")
                    } else {
                        return AuthResult.Failure("Already accepted")
                    }
                } else {
                    return AuthResult.Failure("Invalid invitation data")
                }
            } else {
                return AuthResult.Failure("Invitation not found")
            }
        } catch (e: Exception) {
            return AuthResult.Exception(e)
        }
    }

    override suspend fun removeInvitation(invitationId: String) {
        if (invitationId.isBlank()) {
            throw IllegalArgumentException("Invalid invitation ID")
        }
        try {
            // Ensure the document ID is correct and corresponds to a specific invitation
            val invitationRef = firestore.collection("invitations").document(invitationId)
            invitationRef.delete().await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to remove invitation", e)
        }
    }


    override suspend fun fetchInvites(): List<Invite> {
        try {
            val invitesRef = firestore.collection("invitations")
            val snapshot = invitesRef.whereEqualTo("receiverEmail", auth.currentUser?.email).get().await()

            return snapshot.documents.mapNotNull { document ->
                document.toObject(Invite::class.java)
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch invitations", e)
        }
    }


    override suspend fun fetchEmployees(companyId: String): List<User> {
        // Check if companyId is null or empty
        if (companyId.isBlank()) {
            return emptyList()
        }

        try {
            val companyRef = firestore.collection("users").document(companyId)
            val employeesSnapshot = companyRef.collection("employees").get().await()

            // Handle empty or missing employees gracefully
            return employeesSnapshot.documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch employees", e)
        }
    }




    override suspend fun removeEmployee(email: String) {
        try {
            val employeeSnapshot = firestore.collection("users").whereEqualTo("email", email).get().await()
            val employeeDoc = employeeSnapshot.documents.firstOrNull()

            if (employeeDoc != null) {
                val userId = employeeDoc.id

                firestore.collection("users").document(currentUserId)
                    .collection("employees").document(userId).delete().await()

                firestore.collection("users").document(userId)
                    .update("companyId", FieldValue.delete(), "role", FieldValue.delete()).await()

            } else {
                throw IllegalStateException("Employee not found")
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to remove employee", e)
        }
    }

    override suspend fun leaveCompany() {
        try {
            val currentUserDoc = firestore.collection("users").document(currentUserId).get().await()
            val companyId = currentUserDoc.getString("companyId")

            if (companyId != null) {
                firestore.collection("users").document(companyId)
                    .collection("employees").document(currentUserId).delete().await()

                firestore.collection("users").document(currentUserId)
                    .update("companyId", FieldValue.delete(), "role", FieldValue.delete()).await()

            } else {
                throw IllegalStateException("User is not part of any company")
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to leave company", e)
        }
    }

    private suspend fun checkPremium(): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(currentUserId).get().await()
            documentSnapshot.getBoolean("premium") ?: false
        } catch (e: Exception) {
            false
        }
    }

    override  fun logout() {
        auth.signOut()
    }
    sealed class AuthResult {
        data class Success(val message: String) : AuthResult()
        data class Failure(val error: String) : AuthResult()
        data class Exception(val exception: Throwable) : AuthResult()
    }
    data class Invitation(
        val senderId: String,
        val receiverEmail: String,
        val token: String,
        val status: String,  // e.g., "pending", "accepted", "expired"
        val expirationDate: Long,
        val role: String
    )
}