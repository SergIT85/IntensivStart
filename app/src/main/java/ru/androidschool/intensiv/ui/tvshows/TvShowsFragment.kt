package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.databinding.TvShowsFragmentBinding
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {
    private var _binding: TvShowsFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TvShowsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getTvPopular = MovieApiClient.apiClient.getTvPopular(THE_MOVIE_DATABASE_API,
            "ru", "1")

        lateinit var tvShows: List<Movie>

        getTvPopular.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, error: Throwable) {
                // Логируем ощибку
                Timber.tag("TAGERROR").e(error.toString())
            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                tvShows = response.body()!!.results

                val tvShowsList = tvShows.map {
                    TvShowsItem(it) { movie ->
                        openMovieDetails(movie)
                } }.toList()

                Timber.tag("TAGIIIIIII").e("tvShows ------- $tvShows.toString()")
                Timber.tag("TAGIIIIIII").e("tvShowsList+++++++++++ $tvShowsList.toString()")

                    binding.tvShowsRecyclerView.adapter = adapter.apply { addAll(tvShowsList) }
            }
        })

        val listTvShow = MockRepository.getMovies().map {
            TvShowsItem(it) { movie ->
                openMovieDetails(movie)
            }
        }.toList()
        Timber.tag("TAGIIIIIII").e("listTvShow ============== $listTvShow.toString()")
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
    companion object {
        const val RATING = "rating"
        const val KEY_TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTERPATH = "posterPath"
        const val ID = "id"
    }
}
