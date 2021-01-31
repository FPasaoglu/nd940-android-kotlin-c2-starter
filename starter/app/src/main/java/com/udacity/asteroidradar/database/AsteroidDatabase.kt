package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase(){

    abstract val asteroidDao : AsteroidDao


    companion object {

        @Volatile
        private lateinit var INSTANCE : AsteroidDatabase


        fun getDatabase(context: Context) : AsteroidDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized){
                    INSTANCE = Room.databaseBuilder(
                            context,
                            AsteroidDatabase::class.java,
                            "asteroid_database"
                    ).build()
                }
                return INSTANCE
            }
        }

    }

}