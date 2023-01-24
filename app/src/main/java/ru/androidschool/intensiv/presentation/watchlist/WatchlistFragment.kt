package ru.androidschool.intensiv.Presentation.watchlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.MovieDatabase
import ru.androidschool.intensiv.data.dto.MovieEntity
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding
import ru.androidschool.intensiv.extension.extSingle
import timber.log.Timber

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null

    private lateinit var watchlistViewModel: WatchlistViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult", "FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }

        // получаем доступ к БД
        val databaseMovie = MovieDatabase.get(requireContext())

        val list = databaseMovie.movieDao().getMovieEntity()

        //Созаём ViewModel
        val watchListViewModelFactory = WatchListViewModelFactory(WatchlistRepository(databaseMovie))
        watchlistViewModel = ViewModelProvider(this,
            watchListViewModelFactory)[WatchlistViewModel::class.java]

        var arrayListMovie: ArrayList<MoviePreviewItem> = ArrayList()


        // Работает. Подписывается, но! при изменении базы фильмы дублируются! Не могу понять почему
        // Пробовал и через LiveData и Mutable и создавать Live data в ViewModel и в Repository
        // итого либо не обновляется вообще, либо дублируется.
        watchlistViewModel.getWatchlistFromViewModelTwo().observe(this, Observer { listMovie ->
            arrayListMovie.clear()

            arrayListMovie = listMovie.map {
                MoviePreviewItem(it) {action ->
                    openMovieDetails(action)
                }
            } as ArrayList<MoviePreviewItem>
            binding.moviesRecyclerView.adapter = adapter.apply { addAll(arrayListMovie) }
            Timber.tag("TAGERRORINVIEW").e(listMovie.toString())
        })

        //оставил пока не сделаю как надо.
        /*viewLifecycleOwner.lifecycleScope.launch {
            binding.moviesRecyclerView.adapter = adapter.apply { addAll(watchlistViewModel.
            getWatchlistFromViewModelTwo()) }
        }*/

        //watchlistViewModel.getWatchlistFromViewModelTwo()

        /*var arrayListMovie: ArrayList<MoviePreviewItem> = ArrayList()

        databaseMovie.movieDao().getMovieEntity()
            .extSingle()
            .subscribe({ movie ->
                arrayListMovie.clear()
                arrayListMovie = movie.map {
                    MoviePreviewItem(it) { action ->
                        openMovieDetails(action)
                    }
                } as ArrayList<MoviePreviewItem>
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(arrayListMovie) }
            }, {
                    error -> Timber.tag("TAGERROR").e(error.toString())
            })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
        const val RATING = "rating"
        const val KEY_TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTERPATH = "posterPath"
        const val ID = "id"
    }

    private fun openMovieDetails(movie: MovieEntity) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, movie.title)
        bundle.putFloat(RATING, movie.rating ?: 0.00F)
        bundle.putString(OVERVIEW, movie.overview)
        bundle.putString(POSTERPATH, movie.posterPath)
        bundle.putInt(ID, movie.id.toInt())
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}
