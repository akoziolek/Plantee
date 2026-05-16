package com.example.plantee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plantee.data.local.dto.PlantSummaryDto
import com.example.plantee.data.local.relations.PlantWithDetails
import com.example.plantee.data.local.entities.PlantEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlantsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: PlantEntity): Long

    @Update
    suspend fun update(plant: PlantEntity)

    @Query("UPDATE plants SET is_favourite = NOT is_favourite WHERE id = :id")
    suspend fun updateFavouriteStatus(id: Long)

    @Query("UPDATE plants SET id_media = :mediaId WHERE id = :id")
    suspend fun updateMediaId(id:Long, mediaId: Long?)

    @Delete
    suspend fun delete(plant: PlantEntity)

    @Query("DELETE FROM plants WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Transaction
    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAllFullPlants(): Flow<List<PlantWithDetails>>

    @Transaction
    @Query("SELECT * FROM plants WHERE id IN (:ids) ORDER BY id DESC")
    fun getPlantsByIds(ids: List<Long>): Flow<List<PlantWithDetails>>

    @Query("""
        SELECT p.id, p.name, p.description, p.is_favourite, m.id AS media_id, m.file_path AS media_file_path
        FROM plants p
        LEFT JOIN media m ON m.id = p.id_media
        WHERE p.name LIKE '%' || :searchQuery || '%'
        """)
    fun searchPlants(searchQuery: String): Flow<List<PlantSummaryDto>>

    @Query("""
        SELECT p.id, p.name, p.description, p.is_favourite, m.id AS media_id, m.file_path AS media_file_path
        FROM plants p
        LEFT JOIN media m ON m.id = p.id_media
        WHERE p.name LIKE '%' || :searchQuery || '%'
        ORDER BY p.name ASC""")
    fun searchPlantsAsc(searchQuery: String): Flow<List<PlantSummaryDto>>

    @Query("""
        SELECT p.id, p.name, p.description, p.is_favourite, m.id AS media_id, m.file_path AS media_file_path
        FROM plants p
        LEFT JOIN media m ON m.id = p.id_media
        WHERE p.name LIKE '%' || :searchQuery || '%'
        ORDER BY p.name DESC""")
    fun searchPlantsDesc(searchQuery: String): Flow<List<PlantSummaryDto>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlant(id: Long): Flow<PlantEntity?>

    @Transaction
    @Query("SELECT * FROM plants WHERE id = :id")
    fun getFullPlant(id: Long): Flow<PlantWithDetails?>
}
