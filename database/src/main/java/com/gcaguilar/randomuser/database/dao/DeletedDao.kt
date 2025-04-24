package com.gcaguilar.randomuser.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcaguilar.randomuser.database.entity.DeleteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedDao {
    @Query("SELECT * FROM deleted")
    fun getAllDeletedUsers(): Flow<List<DeleteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deleteEntity: DeleteEntity)
}
