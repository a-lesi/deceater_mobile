package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.Menu
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.lang.reflect.Type

class MenuRepository(var httpClient: OkHttpClient = OkHttpClient()) {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api";

    fun getMenues() : List<Menu>{
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
            .url(url + "/menu")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body().string()
            val type: Type = object : TypeToken<List<Menu?>?>(){}.type

            val gson = Gson()
            val menues: List<Menu> = gson.fromJson(json, type)

            return menues
        }

        return listOf<Menu>()
    }
}