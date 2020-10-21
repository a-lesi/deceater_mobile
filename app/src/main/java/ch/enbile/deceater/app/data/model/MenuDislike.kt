package ch.enbile.deceater.app.data.model

import java.time.LocalDate

data class MenuDislike (
    var transaction_id: Long,
    var commitDate: LocalDate,
    var menu: Menu,
    var user: User
)