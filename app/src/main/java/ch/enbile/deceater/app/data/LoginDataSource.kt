package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.LoggedInUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(var httpClient: OkHttpClient = getUnsafeOkHttpClient()!!)  {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api/user";

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            var credential = Credentials.basic(username, password)

            val request: Request = Request.Builder()
                .addHeader("Authorization", credential)
                .url("$url/authenticate")
                .build()

            val response = httpClient.newCall(request).execute();
            if(response?.isSuccessful!!) {
                val json = response.body?.string()
                val type: Type = object : TypeToken<LoggedInUser?>() {}.type
                val user : LoggedInUser = Gson().fromJson(json, type)
                user.credential = credential
                return Result.Success(user)
            }

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }

        return Result.Error(IOException("Authentication failed"))
    }

    fun logout() {
        // TODO: revoke authentication
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
        builder.connectTimeout(120, TimeUnit.SECONDS)
        builder.build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}
