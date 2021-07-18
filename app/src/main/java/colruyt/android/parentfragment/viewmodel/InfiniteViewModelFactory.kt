package colruyt.android.parentfragment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import colruyt.android.parentfragment.repository.InfiniteRepository
import kotlinx.coroutines.Dispatchers

class InfiniteViewModelFactory : ViewModelProvider.Factory {
    lateinit var  infiniteRepository: InfiniteRepository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        infiniteRepository = InfiniteRepository()
        if (modelClass.isAssignableFrom(InfiniteListViewModel::class.java)) {
            return InfiniteListViewModel(Dispatchers.IO,infiniteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}