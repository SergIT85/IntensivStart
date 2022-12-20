package ru.androidschool.intensiv.ui.feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.extension.extSingle
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null
    val compositeDisposable = CompositeDisposable()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }

        // Получаем Single
        val getMovieNowPlaying = MovieApiClient.apiClient.getMovieNowPlaying(THE_MOVIE_DATABASE_API,
        "ru", "1")

        compositeDisposable.add(getMovieNowPlaying
            .extSingle()
            .subscribe({ movies ->
                val plaingMovie = movies.results

                val listMovie = listOf(plaingMovie.map {
                MovieItem(it) { movie ->
                    openMovieDetails(movie)
                }
                }.let {
                    MainCardContainer(R.string.recommended,
                    it.toList())
                })
                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listMovie) }
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }, {
                    error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
            }))

        val getMoviePopular = MovieApiClient.apiClient.getMoviePopular(THE_MOVIE_DATABASE_API,
            "ru", "1")

        compositeDisposable.add(getMoviePopular
            .extSingle()
            .subscribe({ movies ->
                val plaingMovie = movies.results

                val listMovie = listOf(plaingMovie.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.let {
                    MainCardContainer(R.string.popular,
                        it.toList())
                })
                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listMovie) }
                }
            }, {
                    error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
            }))

        //добавил т.к. забыл сделать в прошлых домашках
        val getUpcoming = MovieApiClient.apiClient.getUpcoming(THE_MOVIE_DATABASE_API, "ru",
        "1")

        compositeDisposable.add(getUpcoming
            .extSingle()
            .subscribe({ movies ->
                val upcomingMovies = movies.results

                val listUpcomingMovies = listOf(upcomingMovies.map {
                    MovieItem(it) {movie ->
                        openMovieDetails(movie)
                    }
                }.let {
                    MainCardContainer(R.string.upcoming,
                    it.toList())
                })
                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listUpcomingMovies) }
                }
            }, {
                    error -> Timber.tag("TAGERROR").e(error.toString())

            }))
    }

    open fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()

        bundle.putString(KEY_TITLE, movie.title)
        bundle.putFloat(RATING, movie.rating)
        bundle.putString(OVERVIEW, movie.overview)
        bundle.putInt(ID, movie.id)
        bundle.putString(POSTERPATH, movie.posterPath)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        searchBinding.searchToolbar.clear()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
    }

    companion object {
        const val RATING = "rating"
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
        const val OVERVIEW = "overview"
        const val POSTERPATH = "posterPath"
        const val ID = "id"
    }
}
