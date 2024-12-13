package com.bangkit.spotlyze.data.local.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.local.database.entity.UserEntity

@Dao
interface SkincareDao {
    @RawQuery(observedEntities = [SkincareEntity::class])
    fun getAllSkincare(query: SupportSQLiteQuery): LiveData<List<SkincareEntity>>

    @Query("SELECT * FROM skincare WHERE favorite = 1")
    fun getFavoriteSkincare(): LiveData<List<SkincareEntity>>

    @Query("SELECT * FROM skincare WHERE type = :type")
    fun getSkincareByType(type: String): LiveData<List<SkincareEntity>>

    @Query("SELECT * FROM skincare WHERE skincare_id = :id")
    fun getSkincareById(id: Int): LiveData<List<SkincareEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): LiveData<UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSkincare(skincare: List<SkincareEntity>)

    @Update
    suspend fun updateSkincare(skincare: SkincareEntity)

    @Query("DELETE FROM skincare WHERE favorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM skincare WHERE skincare_id = :id AND favorite = 1)")
    suspend fun isSkincareFavorite(id: Int): Boolean

}