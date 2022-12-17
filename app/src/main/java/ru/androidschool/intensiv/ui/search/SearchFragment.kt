package ru.androidschool.intensiv.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.databinding.FragmentSearchBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.tvshows.TvShowsItem
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        searchBinding.searchToolbar.setText(searchTerm)

        searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                getSearchMovie(it.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
    }

    companion object {
        const val MIN_LENGTH = 3
        const val RATING = "rating"
        const val KEY_TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTERPATH = "posterPath"
        const val ID = "id"
    }

    //Сделал поиск с использованием RxJava. Понимаю что надо доделывать с реализацией onNext??
    // и удалить для очистки памяти при выходе, го пока пробую как и что.
    @SuppressLint("CheckResult")
    fun getSearchMovie(query: String) {
        val getSearchMovie = MovieApiClient.apiClient.getSearchMovie(
            BuildConfig.THE_MOVIE_DATABASE_API,
            "ru", query)

        getSearchMovie
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                val movieResulrSerch = result.results

                val listMovieResult = movieResulrSerch.map {
                    TvShowsItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.toList()
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(listMovieResult) }
            }, { error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
            })
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, movie.name)
        bundle.putFloat(RATING, movie.rating)
        bundle.putString(OVERVIEW, movie.overview)
        bundle.putString(POSTERPATH, movie.posterPath)
        bundle.putInt(ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }
}
