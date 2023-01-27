package ru.androidschool.intensiv.Presentation.watchlist

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import kotlinx.coroutines.Job
import ru.androidschool.intensiv.data.repository.WatchlistRepository
import ru.androidschool.intensiv.data.roomdata.MovieEntity
import ru.androidschool.intensiv.extension.extObservable


@SuppressLint("CheckResult")
class WatchlistViewModel(private val repository: WatchlistRepository): ViewModel() {

    private var searchJob: Job? = null

    // Создаём MutableLiveData для передачи данных в View
    var watchlistViewModel: MutableLiveData<List<MovieEntity>> = MutableLiveData()

    init {
        getWatchlistFromViewModelTwo().extObservable()
            .subscribe {
                watchlistViewModel.value = it
            }
    }

    fun getWatchlistFromViewModelTwo() : Observable<List<MovieEntity>>{
        return repository.getWatchlist()
    }
}