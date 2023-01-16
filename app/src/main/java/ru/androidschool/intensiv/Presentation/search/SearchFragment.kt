package ru.androidschool.intensiv.Presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.databinding.FragmentSearchBinding
import ru.androidschool.intensiv.extension.extObservable
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.Presentation.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.Presentation.feed.MovieItem
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!
    val compositeDisposable = CompositeDisposable()

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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        searchBinding.searchToolbar.setText(searchTerm)

// реализация поиска. Работает с задержкой на нужное время!!
// пришлось два раза создавать поток и подписыватсья для реализации задержки,
// возможно можно сделать проще, но пока оставлю так т.к. есть новая домашка
        val onTextChangeObservable by lazy {
            Observable.create(
                ObservableOnSubscribe<String> { emiter ->
                    searchBinding.searchToolbar.binding.searchEditText.doAfterTextChanged { text ->
                        emiter.onNext(text.toString())
                    }
                }
            )
        }
        val onTextChangeWithOperatorObservable by lazy {
            onTextChangeObservable
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { it.trim() }
                .filter { it.length > MIN_LENGTH }
                .observeOn(AndroidSchedulers.mainThread())
        }

        compositeDisposable.add(onTextChangeWithOperatorObservable
            .extObservable()
            .subscribe { query ->
            val getSearchMovie = MovieApiClient.apiClient.getSearchMovie(
                BuildConfig.THE_MOVIE_DATABASE_API,
                "ru", query
            )

            getSearchMovie
                .extObservable()
                .subscribe({ result ->
                val movieResulrSerch = result.results

                val listMovieResult = movieResulrSerch.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.toList()
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(listMovieResult) }
                binding.progressBar.visibility = View.INVISIBLE
            }, { error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
        compositeDisposable.clear()
    }

    companion object {
        const val MIN_LENGTH = 3
        const val RATING = "rating"
        const val KEY_TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTERPATH = "posterPath"
        const val ID = "id"
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
