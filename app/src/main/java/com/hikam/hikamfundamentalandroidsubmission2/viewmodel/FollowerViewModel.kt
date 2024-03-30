package com.hikam.hikamfundamentalandroidsubmission2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowerViewModel: ViewModel() {
    private val _itemsItem = MutableLiveData<List<ItemsItem?>?>()
    val itemsItem: LiveData<List<ItemsItem?>?> = _itemsItem

    private val _followerUsers = MutableLiveData<List<ItemsItem>>()
    val usersFollower: LiveData<List<ItemsItem>> = _followerUsers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun printFollowers(username: String?) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getFollowers(username.toString()).enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followerUsers.value = response.body()
                } else {
                    _error.postValue("API Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _error.postValue("API Failure: ${t.message}")
            }
        })
    }
}