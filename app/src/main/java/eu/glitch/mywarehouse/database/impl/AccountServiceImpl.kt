package eu.glitch.mywarehouse.database.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.glitch.mywarehouse.database.IAccountService
import eu.glitch.mywarehouse.model.User
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) :
    IAccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null
    override val currentUser: User
        get() = runBlocking {
            val authUser = auth.currentUser
            if (authUser != null) {
                val isPremium = checkPremium()
                User(authUser.uid, authUser.email ?: "", isPremium)
            } else {
                User()
            }
        }
    override val hasPremium: Boolean
        get() = runBlocking {checkPremium()}

    override suspend fun authenticate(email: String, password: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (continuation.isActive) { // Ensure it is not already resumed
                            continuation.resume(true)
                        }
                    } else {
                        if (continuation.isActive) {
                            continuation.resume(false)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }


    override fun register(email: String, password: String) : Boolean{
        auth.createUserWithEmailAndPassword(email,password)
        return true
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


}