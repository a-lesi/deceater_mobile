package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.LoggedInUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(var httpClient: OkHttpClient = OkHttpClient()) {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api/user";

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            var credential = Credentials.basic(username, password)

            val request: Request = Request.Builder()
                .header("Authorization", "your token")
                .url("$url/authenticate")
                .addHeader("Authorization", credential)
                .build()

                val response = httpClient.newCall(request).execute()

                if(response.isSuccessful) {
                    val json = response.body().string()
                    val type: Type = object : TypeToken<LoggedInUser?>() {}.type
                    val user : LoggedInUser = Gson().fromJson(json, type)
                    return Result.Success(user)
                }

            return Result.Error(IOException("Authentication failed"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}