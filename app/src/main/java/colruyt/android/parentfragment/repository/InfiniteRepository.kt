package colruyt.android.parentfragment.repository

import colruyt.android.parentfragment.model.InfiniteModel
import colruyt.android.parentfragment.utils.JSONHelper
import retrofit2.Response

class InfiniteRepository {
    suspend  fun fetchInfiniteList() : Response<InfiniteModel> = JSONHelper.fetchParsedJSONInfoForInfiniteList()
}