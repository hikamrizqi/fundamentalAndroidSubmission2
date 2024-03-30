package com.hikam.hikamfundamentalandroidsubmission2.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.data.helper.FavoriteGithubUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.data.helper.FavoriteGithubUserRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val favoriteGithubUserRepository: FavoriteGithubUserRepository =
        FavoriteGithubUserRepository(application)

    fun insert(favoriteUser: FavoriteGithubUserEntity) {
        favoriteGithubUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteGithubUserEntity) {
        favoriteGithubUserRepository.delete(favoriteUser)
    }
}