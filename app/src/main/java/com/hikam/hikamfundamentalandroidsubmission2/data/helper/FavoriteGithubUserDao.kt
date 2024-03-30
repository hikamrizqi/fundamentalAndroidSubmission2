package com.hikam.hikamfundamentalandroidsubmission2.data.helper

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteGithubUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteGithubUserEntity)

    @Update
    fun update(favoriteUser: FavoriteGithubUserEntity)

    @Delete
    fun delete(favoriteUser: FavoriteGithubUserEntity)

    @Query("SELECT * FROM FavoriteGithubUserEntity")
    fun getAllFavoriteUser(): LiveData<List<FavoriteGithubUserEntity>>

    @Query("SELECT * FROM FavoriteGithubUserEntity WHERE username = :username")
    fun getFavoriteUsername(username: String): LiveData<FavoriteGithubUserEntity>
}