package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getDayFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(val database: AsteroidDatabase) {


    val yesterday = getDayFormattedDates(-1)
    val today = getDayFormattedDates(0)
    val week = getDayFormattedDates(7)


    var asteroids = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }
    var asteroidToday = Transformations.map(database.asteroidDao.getAsteroidWithDate(today)) {
        it.asDomainModel()
    }
    val asteroidWeekly =
        Transformations.map(database.asteroidDao.getAsteroidsBetweenDate(today, week)) {
            it.asDomainModel()
        }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    suspend fun refreshAsteroids() {
        try {
            withContext(Dispatchers.IO) {
                val asteroid =
                    parseAsteroidsJsonResult(JSONObject(Network.asteroidService.getNearAstreoids()))
                database.asteroidDao.insertAll(*asteroid.asDatabaseModel())
                // live data can not set value in background thread
                _pictureOfDay.postValue(Network.pictureOfDayApiService.getPictureOfDay())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroidWithDate(yesterday)
        }
    }

}