package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class VIEW_FILTER { TODAY, WEEK, ALL }

class MainViewModel(application: Application) : AndroidViewModel(application) {


    val database = AsteroidDatabase.getDatabase(application)
    val repository = AsteroidRepository(database)

    val pictureOfDay = repository.pictureOfDay

    val asteroidFilter = MutableLiveData<VIEW_FILTER>(VIEW_FILTER.WEEK)
    val asteroids = Transformations.switchMap(asteroidFilter) {
        when (it) {
            VIEW_FILTER.TODAY -> repository.asteroidToday
            VIEW_FILTER.WEEK -> repository.asteroidWeekly
            else -> repository.asteroids
        }
    }

    init {
        refreshServices()
        deleteOldAsteroids()
    }

    fun refreshServices() {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    fun deleteOldAsteroids() {
        viewModelScope.launch {
            repository.deleteOldAsteroids()
        }
    }

    fun updateViewFilter(viewFilter: VIEW_FILTER) {
        asteroidFilter.value = viewFilter
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("no found view model class")
        }

    }
}

