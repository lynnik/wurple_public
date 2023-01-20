package com.wurple.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wurple.data.model.user.UserStorageData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userStorageData: UserStorageData)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: String): UserStorageData?

    @Query("SELECT * FROM user WHERE id = :id")
    fun observeUserById(id: String): Flow<UserStorageData?>

    @Query("DELETE FROM user")
    fun clearCache()
}