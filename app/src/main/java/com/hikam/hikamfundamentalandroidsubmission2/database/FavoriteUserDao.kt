package com.hikam.hikamfundamentalandroidsubmission2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUserEntity)

    @Update
    fun update(favoriteUser: FavoriteUserEntity)

    @Delete
    fun delete(favoriteUser: FavoriteUserEntity)

    @Query("SELECT * FROM FavoriteUserEntity")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM FavoriteUserEntity WHERE username = :username")
    fun getFavoriteUsername(username: String): LiveData<FavoriteUserEntity>
}