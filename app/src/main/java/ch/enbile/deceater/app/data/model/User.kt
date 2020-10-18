package ch.enbile.deceater.app.data.model

data class User (
    var username: String,
    var firstname: String,
    var lastname: String,
    var password: String,
    var enabled : Boolean
)