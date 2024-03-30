package com.hikam.hikamfundamentalandroidsubmission2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.data.response.DetailGithubUserResponse
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserModel: ViewModel() {
    private val _itemsItem = MutableLiveData<List<ItemsItem?>?>()
    val itemsItem: LiveData<List<ItemsItem?>?> = _itemsItem

    private val _detailUser = MutableLiveData<DetailGithubUserResponse>()
    val detailUser: LiveData<DetailGithubUserResponse> = _detailUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUserDetail(username: String?){
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getDetailUser(username.toString()).enqueue(object : Callback<DetailGithubUserResponse> {
            override fun onResponse(
                call: Call<DetailGithubUserResponse>,
                response: Response<DetailGithubUserResponse>
            )
            {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    _error.postValue("API Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailGithubUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.postValue("API Failure: ${t.message}")
            }
        })
    }
}