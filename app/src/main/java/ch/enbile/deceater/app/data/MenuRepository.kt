package ch.enbile.deceater.app.data

import ch.enbile.deceater.app.data.model.Menu
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import java.lang.reflect.Type

class MenuRepository(var httpClient: OkHttpClient = OkHttpClient()) {
    val url = "https://enbile.westeurope.cloudapp.azure.com/deceater/api/menu";

    fun getMenues() : List<Menu>{
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
            .url("$url")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body().string()
            val type: Type = object : TypeToken<List<Menu?>?>(){}.type

            val gson = Gson()
            val menues: List<Menu> = gson.fromJson(json, type)

            return menues
        }

        return listOf()
    }


    fun getDailyMenu() : Menu? {
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
            .url("$url/getTodaysMenu")
            .build()

        val response = httpClient.newCall(request).execute()

        if(response.isSuccessful){
            val json = response.body().string()
            val type: Type = object : TypeToken<Menu?>(){}.type

            val gson = Gson()
            val menu: Menu = gson.fromJson(json, type)

            return menu
        }

        return null
    }

    fun addMenu(menu: Menu) : Boolean{
        var requestBody : RequestBody = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(menu))
        val request: Request = Request.Builder()
            .header("Authorization", "your token")
            .url("$url/add")
            .method("post", requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }
}