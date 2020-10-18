package ch.enbile.deceater.app.data.model

data class Menu (
    var menu_id: Long,
    var name: String,
    var description: String,
    var location: String,
    var disliked: Boolean
)