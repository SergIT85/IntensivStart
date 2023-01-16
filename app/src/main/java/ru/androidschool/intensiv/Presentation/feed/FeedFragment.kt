package ru.androidschool.intensiv.Presentation.feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import ru.androidschool.intensiv.Domain.MovieEminClass
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.extension.extSingle
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.Presentation.afterTextChanged
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieResponse
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

        // Сделал с hashMap! ура=)

        val nawPlaing = MovieApiClient.apiClient.getMovieNowPlaying()

        val getUpcomings = MovieApiClient.apiClient.getUpcoming()

        val getMoviePopulars = MovieApiClient.apiClient.getMoviePopular()

        val hashMapOllMovie = Single.zip(nawPlaing, getUpcomings, getMoviePopulars, Function3<
                MovieResponse,
                MovieResponse,
                MovieResponse, HashMap<String, List<Movie>>> {
                t1: MovieResponse, t2: MovieResponse, t3: MovieResponse ->
            hashMapOf(MovieEminClass.NAWPLAING.toString() to t1.results,
                MovieEminClass.UPCOMING.toString() to t2.results,
                MovieEminClass.POPULAR.toString() to t3.results)
        })

        compositeDisposable.add(hashMapOllMovie
            .extSingle()
            .subscribe({ movies ->

                val listPopular = createMainCardContainer(movies.getValue(MovieEminClass.POPULAR.toString()), R.string.popular)
                val listUpcomings = createMainCardContainer(movies.getValue(MovieEminClass.UPCOMING.toString()), R.string.upcoming)
                val listNawPlaing = createMainCardContainer(movies.getValue(MovieEminClass.NAWPLAING.toString()), R.string.recommended)

                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listNawPlaing) }
                    binding.progressBar.visibility = View.INVISIBLE
                }
                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listUpcomings) }
                }
                movies.let {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(listPopular) }
                }
            }, {
                    error -> Timber.tag("TAGERROR").e(error.toString())
            })
        )
    }

    fun createMainCardContainer(listMovie: List<Movie>, tipeMovie: Int): List<MainCardContainer> {
        val list = listOf(listMovie.map {
            MovieItem(it) { movie ->
                openMovieDetails(movie)
            }
        }.let {
            MainCardContainer(tipeMovie, it.toList())
        })
        return list
    }

    fun openMovieDetails(movie: Movie) {
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
