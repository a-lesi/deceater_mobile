package ch.enbile.deceater.app.ui.login

import android.net.wifi.hotspot2.pps.Credential
import android.os.Parcel
import android.os.Parcelable
import ch.enbile.deceater.app.data.model.LoggedInUser

/**
 * User details post authentication that is exposed to the UI
 */
class LoggedInUserView() : Parcelable {
        var username: String? = null
        var firstName: String? = null
        var lastname: String? = null
        var credential: String? = null

        constructor(parcel: Parcel) : this() {
                username = parcel.readString()
                firstName = parcel.readString()
                lastname = parcel.readString()
                credential = parcel.readString()
        }

        constructor(username:String?, firstName:String?, lastName:String?, credential: String?) : this(){
                this.username = username
                this.firstName = firstName
                this.lastname = lastName
                this.credential = credential
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(username)
                parcel.writeString(firstName)
                parcel.writeString(lastname)
                parcel.writeString(credential)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<LoggedInUserView> {
                override fun createFromParcel(parcel: Parcel): LoggedInUserView {
                        return LoggedInUserView(parcel)
                }

                override fun newArray(size: Int): Array<LoggedInUserView?> {
                        return arrayOfNulls(size)
                }
        }
}