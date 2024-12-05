package com.bangkit.spotlyze.data.local.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity

@Dao
interface SkincareDao {
    @Query("SELECT * FROM skincare")
    fun getAllSkincare(): LiveData<List<SkincareEntity>>

    @Query("SELECT * FROM skincare WHERE favorite = 1")
    fun getFavoriteSkincare(): LiveData<List<SkincareEntity>>

    @Query("SELECT * FROM skincare WHERE skincare_id = :id")
    fun getSkincareById(id: Int): LiveData<List<SkincareEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSkincare(skincare: List<SkincareEntity>)

    @Update
    suspend fun updateSkincare(skincare: SkincareEntity)

    @Query("DELETE FROM skincare WHERE favorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM skincare WHERE skincare_id = :id AND favorite = 1)")
    suspend fun isSkincareFavorite(id: Int): Boolean

}