package ch.enbile.deceater.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Menu(
    val menuId: Int,
    val userId: Int,
    val displayName: String,
    var disliked: Boolean = false
)