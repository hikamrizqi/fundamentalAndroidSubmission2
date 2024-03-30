package com.hikam.hikamfundamentalandroidsubmission2.data.helper

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteGithubUserRepository(application: Application) {
    private val favoriteUserDao: FavoriteGithubUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteGithubUserEntity>> = favoriteUserDao.getAllFavoriteUser()

    fun getFavoriteUsername(username: String): LiveData<FavoriteGithubUserEntity> =
        favoriteUserDao.getFavoriteUsername(username)

    fun insert(favoriteUser: FavoriteGithubUserEntity) {
        executorService.execute { favoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteGithubUserEntity) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }

    fun update(favoriteUser: FavoriteGithubUserEntity) {
        executorService.execute { favoriteUserDao.update(favoriteUser) }
    }
}