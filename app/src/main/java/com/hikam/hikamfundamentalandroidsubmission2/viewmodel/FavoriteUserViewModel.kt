package com.hikam.hikamfundamentalandroidsubmission2.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.database.FavoriteUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>> =
        mFavoriteUserRepository.getAllFavoriteUser()

    fun getFavoriteUsername(username: String): LiveData<FavoriteUserEntity> =
        mFavoriteUserRepository.getFavoriteUsername(username)
}