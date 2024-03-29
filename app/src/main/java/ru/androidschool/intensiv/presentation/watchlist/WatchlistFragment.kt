package ru.androidschool.intensiv.Presentation.watchlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.repository.WatchlistRepository
import ru.androidschool.intensiv.data.roomdata.MovieDatabase
import ru.androidschool.intensiv.data.roomdata.MovieEntity
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding
import ru.androidschool.intensiv.presentation.watchlist.MoviePreviewItem
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

        // Работает. через MVVM и LiveData
        watchlistViewModel.watchlistViewModel.observe(this, Observer { listMovie ->
            arrayListMovie.clear()

            arrayListMovie = listMovie.map {
                MoviePreviewItem(it) {action ->
                    openMovieDetails(action)
                }
            } as ArrayList<MoviePreviewItem>
            adapter.clear()
            binding.moviesRecyclerView.adapter = adapter.apply { addAll(arrayListMovie) }
            Timber.tag("TAGERRORINVIEW").e(listMovie.toString())
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        adapter.clear()
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
