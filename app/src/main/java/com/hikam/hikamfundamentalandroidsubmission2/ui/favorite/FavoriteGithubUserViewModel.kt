package com.hikam.hikamfundamentalandroidsubmission2.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.data.helper.FavoriteGithubUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.data.helper.FavoriteGithubUserRepository

class FavoriteGithubUserViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteGithubUserRepository: FavoriteGithubUserRepository =
        FavoriteGithubUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteGithubUserEntity>> =
        mFavoriteGithubUserRepository.getAllFavoriteUser()

    fun getFavoriteUsername(username: String): LiveData<FavoriteGithubUserEntity> =
        mFavoriteGithubUserRepository.getFavoriteUsername(username)
}