package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.Menu
import ch.enbile.deceater.app.data.model.MenuDislike
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

class MenuRepository(var httpClient: OkHttpClient = getUnsafeOkHttpClient()!!) {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api/menu";

    fun getMenues() : List<Menu>{
        val credential = Credentials.basic("chrigi", "chrigi_pass")


        val request: Request = Request.Builder()
            .header("Authorization", credential)
            .url("$url")
            .build()
        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body?.string()
            val type: Type = object : TypeToken<List<Menu?>?>(){}.type
            return Gson().fromJson(json, type)
        }

        return listOf()
    }

    fun getDailyMenu() : Menu? {
        val credential = Credentials.basic("chrigi", "chrigi_pass")

        val request: Request = Request.Builder()
            .addHeader("Authorization", credential)
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
            .header("Authorization", "your token")
            .url("$url/add")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun tryDeleteMenu(menu: Menu) : Boolean{
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
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
            .header("Authorization", "your token")
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
            .header("Authorization", "your token")
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
            .header("Authorization", "your token")
            .url("$url/dislike/remove")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun getPersonalDislike() : MenuDislike?{
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
            .url("$url/dislike")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body?.string()
            val type: Type = object : TypeToken<MenuDislike?>(){}.type
            return Gson().fromJson(json, type)
        }

        return null
    }
}

fun getUnsafeOkHttpClient(): OkHttpClient? {
    return try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            }
        )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager))
        builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        builder.connectTimeout(300, TimeUnit.SECONDS)
        builder.build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}