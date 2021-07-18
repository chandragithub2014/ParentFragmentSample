package colruyt.android.parentfragment.interfaces

import colruyt.android.parentfragment.model.InfiniteAdapterModel
import colruyt.android.parentfragment.model.InfiniteModel


interface LoadMore {
    fun loadMore(startPosition:Int, endPosition:Int, previousList:MutableList<InfiniteAdapterModel>, originalModel: InfiniteModel)
}