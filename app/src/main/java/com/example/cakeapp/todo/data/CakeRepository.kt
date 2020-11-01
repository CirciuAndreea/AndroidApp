package com.example.cakeapp.todo.data

import com.example.cakeapp.core.Api
import com.example.cakeapp.core.Result
import com.example.cakeapp.todo.data.remote.CakeApi

object CakeRepository {
    private var cachedItems: MutableList<Cake>? = null;

    suspend fun loadAll(): Result<List<Cake>> {
        if (cachedItems != null) {
            return Result.Success(cachedItems as List<Cake>);
        }
        try {
            val items = CakeApi.service.find()
            cachedItems = mutableListOf()
            cachedItems?.addAll(items)
            return Result.Success(cachedItems as List<Cake>)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun load(itemId: String): Result<Cake> {
        val item = cachedItems?.find { it._id == itemId }
        if (item != null) {
            return Result.Success(item)
        }
        try {
            return Result.Success(CakeApi.service.read(itemId))
            print("Load with succes")
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun save(item: Cake): Result<Cake> {
        try {
            val createdItem = CakeApi.service.create(item)
            cachedItems?.add(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Cake): Result<Cake> {
        try {
            val updatedItem = CakeApi.service.update(item._id, item)
            val index = cachedItems?.indexOfFirst { it._id == item._id }
            if (index != null) {
                cachedItems?.set(index, updatedItem)
            }
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
    suspend fun delete(itemId: String): Result<Boolean>
    {
        try {

            val index = cachedItems?.indexOfFirst { it._id == itemId }
            if (index!=null)
            {
                cachedItems?.removeAt(index)
            }
            return Result.Success(true)
        }
        catch (e: Exception) {
            return Result.Error(e)
        }
    }
}
