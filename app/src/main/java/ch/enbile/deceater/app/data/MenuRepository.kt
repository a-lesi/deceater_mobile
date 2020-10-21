package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.Menu
import ch.enbile.deceater.app.data.model.MenuDislike
import ch.enbile.deceater.app.ui.login.LoggedInUserView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MenuRepository(var loggedInUser: LoggedInUserView, var httpClient: OkHttpClient = getUnsafeOkHttpClient()!!) {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api/menu";

    fun getMenues() : List<Menu>{
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url")
            .build()
        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body?.string()
            val type: Type = object : TypeToken<List<Menu?>?>(){}.type
            val menues : List<Menu> = Gson().fromJson(json, type)

            val dislike = getPersonalDislike()
            if (dislike != null) {
                val dislikedMenu = menues.find { m -> m.menu_id == dislike.menu_id }
                if (dislikedMenu != null) {
                    dislikedMenu.disliked = true
                };
            }
            return menues
        }

        return listOf()
    }

    fun getDailyMenu() : Menu? {
        val request: Request = Request.Builder()
            .addHeader("Authorization", loggedInUser.credential!!)
            .url("$url/getTodaysMenu")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body?.string()
            val type: Type = object : TypeToken<Menu?>(){}.type
            return Gson().fromJson(json, type)
        }

        return null
    }

    fun tryAddMenu(menu: Menu) : Boolean{
        var requestBody : RequestBody =
            Gson().toJson(menu).toRequestBody("application/json".toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/add")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun tryDeleteMenu(menu: Menu) : Boolean{
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/${menu.menu_id}")
            .delete()
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun tryUpdateMenu(menu: Menu) : Boolean{
        var requestBody : RequestBody =
            Gson().toJson(menu).toRequestBody("application/json".toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/update")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun tryDislikeMenu(menu: Menu) : Boolean{
        var requestBody : RequestBody =
            Gson().toJson(menu).toRequestBody("application/json".toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/dislike/set")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun tryUndislikeMenu(menu: Menu): Boolean{
        var requestBody : RequestBody =
            Gson().toJson(menu).toRequestBody("application/json".toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/dislike/remove")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun getPersonalDislike() : Menu?{
        val request: Request = Request.Builder()
            .header("Authorization", loggedInUser.credential!!)
            .url("$url/dislike")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body?.string()
            val type: Type = object : TypeToken<Menu?>(){}.type
            return Gson().fromJson(json, type)
        }

        return null
    }
}