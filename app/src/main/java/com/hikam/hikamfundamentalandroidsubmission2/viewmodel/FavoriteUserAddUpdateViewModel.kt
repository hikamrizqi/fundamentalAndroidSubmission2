package com.hikam.hikamfundamentalandroidsubmission2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.hikam.hikamfundamentalandroidsubmission2.database.FavoriteUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.repository.FavoriteUserRepository

class FavoriteUserAddUpdateViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUserEntity) {
        favoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUserEntity) {
        favoriteUserRepository.delete(favoriteUser)
    }
}