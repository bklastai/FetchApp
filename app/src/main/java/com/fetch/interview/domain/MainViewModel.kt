package com.fetch.interview.domain

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.util.Supplier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.fetch.interview.FetchApplication
import com.fetch.interview.network.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel(
    private val remoteRepo: Supplier<RemoteRepository>
) : ViewModel() {
    val itemsFlow: SnapshotStateList<FetchItem> = mutableStateListOf()

    suspend fun fetchData() {
        val newData = withContext(Dispatchers.IO) {
            remoteRepo.get().getData()
        }
        if (newData.success) {
            itemsFlow.clear()
            itemsFlow.addAll(newData.content)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel(
                    (application as FetchApplication).remoteRepo::get
                ) as T
            }
        }
    }
}