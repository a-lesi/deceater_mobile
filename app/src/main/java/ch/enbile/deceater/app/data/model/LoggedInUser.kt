package ch.enbile.deceater.app.data.model

import android.net.wifi.hotspot2.pps.Credential
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        var username: String?,
        var firstName: String?,
        var lastname: String?,
        var credential: String?
)
