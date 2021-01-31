package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroid_table order by closeApproachDate")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :startDate order by closeApproachDate")
    fun getAsteroidWithDate(startDate: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table where closeApproachDate between :startDate and :endDate order by closeApproachDate")
    fun getAsteroidsBetweenDate(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid_table WHERE closeApproachDate = :date")
    fun deleteAsteroidWithDate(date: String)
}