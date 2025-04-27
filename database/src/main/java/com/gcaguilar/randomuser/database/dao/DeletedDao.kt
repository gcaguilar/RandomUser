package com.gcaguilar.randomuser.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcaguilar.randomuser.database.entity.DeleteEntity

@Dao
interface DeletedDao {
    @Query("SELECT * FROM deleted")
    fun getAllDeletedUsers(): List<DeleteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deleteEntity: DeleteEntity)
}
