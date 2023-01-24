package ru.androidschool.intensiv.Presentation.watchlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieDatabase
import ru.androidschool.intensiv.data.dto.MovieEntity
import ru.androidschool.intensiv.data.repository.WatchlistRepository
import timber.log.Timber

class WatchlistViewModel(private val repository: WatchlistRepository): ViewModel() {

    private var searchJob: Job? = null

    // Создаём MutableLiveData для передачи данных в View
    var watchlistViewModel: MutableLiveData<List<MovieEntity>>? = null
    //val watchlistViewModelList : LiveData<List<MovieEntity>> = watchlistViewModel

    //lateinit var listMovie: List<MoviePreviewItem>
    //val database = MovieDatabase.get(context = context)

    private fun getWatchlistFromViewModel() {
        //watchlistViewModel = repository.getWatchlist()


        Timber.tag("TAGERROR").e(watchlistViewModel.toString())
    }
    fun getWatchlistFromViewModelTwo() : LiveData<List<MovieEntity>>{
        searchJob?.cancel()
        Timber.tag("TAGERROR").e(repository.getWatchlist().toString())

        return repository.getWatchlist()
    }
    init {
            viewModelScope.launch(Dispatchers.IO) { getWatchlistFromViewModel() }
    }

}