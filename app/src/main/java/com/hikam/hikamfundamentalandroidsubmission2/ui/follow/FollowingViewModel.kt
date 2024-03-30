package com.hikam.hikamfundamentalandroidsubmission2.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    private val _followingUsers = MutableLiveData<List<ItemsItem>>()
    val usersFollowing: LiveData<List<ItemsItem>> = _followingUsers

    private val _error = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun printFollowings(username: String?) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getFollowing(username.toString()).enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingUsers.value = response.body()
                } else {
                    _error.postValue("API Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _error.postValue("API Gagal: ${t.message}")
            }
        })
    }
}