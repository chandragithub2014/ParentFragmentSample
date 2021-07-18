package colruyt.android.parentfragment.utils


import colruyt.android.parentfragment.ReferenceApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import colruyt.android.parentfragment.model.InfiniteModel
import retrofit2.Response
import java.lang.reflect.Type


object  JSONHelper {




    fun fetchParsedJSONInfoForInfiniteList() : Response<InfiniteModel> {
        val jsonfile: String = ReferenceApplication.instance.applicationContext.assets.open("infinitelist.json").bufferedReader().use {it.readText()}
        println("Parsed JSON file is.... $jsonfile")

        val gson = Gson()
        val infiniteListModelType: Type = object : TypeToken<InfiniteModel>() {}.type
        var infiniteModel = gson.fromJson<InfiniteModel>(jsonfile,infiniteListModelType)
        println("Output in String ${infiniteModel.toString()}")
        return Response.success(infiniteModel)
    }



}