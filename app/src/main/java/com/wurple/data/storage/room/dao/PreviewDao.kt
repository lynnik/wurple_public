package com.wurple.data.storage.room.dao

import androidx.room.*
import com.wurple.data.model.preview.PreviewStorageData
import kotlinx.coroutines.flow.Flow

@Dao
interface PreviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(previews: List<PreviewStorageData>)

    @Query("SELECT * FROM preview")
    fun observePreviews(): Flow<List<PreviewStorageData>>

    @Query("DELETE FROM preview")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllAndInsert(previews: List<PreviewStorageData>) {
        deleteAll()
        insert(previews)
    }

    @Query("DELETE FROM preview")
    fun clearCache()
}