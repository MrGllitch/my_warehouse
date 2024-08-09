package eu.glitch.mywarehouse.database

import eu.glitch.mywarehouse.model.User


interface IAccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: User
    val hasPremium: Boolean

    suspend fun authenticate(email: String, password: String) : Boolean

    fun register(email: String, password: String) : Boolean

     fun logout()

}