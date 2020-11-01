package com.example.cakeapp.todo.cakes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.cakeapp.core.Result
import com.example.cakeapp.core.TAG
import com.example.cakeapp.todo.data.Cake
import com.example.cakeapp.todo.data.CakeRepository

class CakeListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<Cake>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Cake>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = CakeRepository.loadAll()) {
                is Result.Success -> {
                    Log.d(TAG, "loadItems succeeded");
                    mutableItems.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadItems failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
