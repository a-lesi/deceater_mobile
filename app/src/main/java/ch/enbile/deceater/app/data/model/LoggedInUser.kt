package ch.enbile.deceater.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        var username : String,
        var firstname: String,
        var lastname: String,
        var password: String,
        var enabled : Boolean = true,
        var roles : Set<Role> = hashSetOf()
)