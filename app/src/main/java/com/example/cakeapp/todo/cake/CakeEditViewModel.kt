package com.example.cakeapp.todo.cake

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

class CakeEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Cake>().apply { value = Cake("", "", countertops = "",cream="",amount = 0,design = false,name = "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Cake> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            Log.v(TAG, "loadItem...");
            mutableFetching.value = true
            mutableException.value = null
            when (val result = CakeRepository.load(itemId)) {
                is Result.Success -> {
                    Log.d(TAG, "loadItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }

    fun saveOrUpdateItem(text: String,countertops: String,cream: String,amount: Number, design: Boolean) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.name = text
            item.countertops=countertops
            item.cream=cream
            item.amount=amount
            item.design=design
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Cake>
            if (item._id.isNotEmpty()) {
                result = CakeRepository.update(item)
            } else {
                result = CakeRepository.save(item)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
    fun deleteItem()
    {
        viewModelScope.launch {
            mutableFetching.value = true
            mutableException.value = null
            val item = mutableItem.value ?: return@launch
            val result: Result<Boolean>
            result = CakeRepository.delete(item._id)
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "delete succeeded");
//                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "delete failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}
