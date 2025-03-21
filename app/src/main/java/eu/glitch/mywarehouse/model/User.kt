package eu.glitch.mywarehouse.model


data class User(
    val id: String = "",
    val email: String = "",
    val companyId: String? = null,
    val role: String? = null
) {
    constructor() : this("", "", null, null)
}