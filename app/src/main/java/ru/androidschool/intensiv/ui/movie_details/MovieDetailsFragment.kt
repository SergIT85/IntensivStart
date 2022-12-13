package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.ActorResponse
import ru.androidschool.intensiv.data.MovieDetails
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.databinding.MovieDetailsHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {
    private var _binding: MovieDetailsFragmentBinding? = null
    private var _headerBinding: MovieDetailsHeaderBinding? = null

    private val headerBinding get() = _headerBinding!!
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        _headerBinding = MovieDetailsHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Постер фильма в хэдере
        Picasso.get()
            .load(requireArguments().getString("posterPath"))
            .fit()
            .centerCrop()
            .into(headerBinding.detailsBackdrop)

        // Кнопка назад в хедере
        val arrow = headerBinding.headerToolbarDetails
        arrow.setOnClickListener {
            activity.let {
                (it as MainActivity).supportFragmentManager.popBackStack()
            }
        }

        // Устанавливаем Название
        val movieTitle = requireArguments().getString("title")

        Timber.tag("TAGIIIIIII").e("listTvShow ============== $movieTitle")
        binding.nameFilmsInDetails.text = movieTitle

        // Устанавливаем описание
        val movieDescription = requireArguments().getString("overview")
        val textView = binding.descriptionMovieDetails
        binding.descriptionMovieDetails.text = movieDescription
        // Алгоритм сворачивания - разворачивания описания
        val quantityTermDescription = 6
        var flag = true
        textView.setOnClickListener {
            if (flag) {
                textView.maxLines = Int.MAX_VALUE
                flag = false
            } else {
                textView.maxLines = quantityTermDescription
                flag = true
            }
        }

        // Устанавливаем рейтинг
        val ratingNull = 5F
        binding.detailMovieRating.rating = arguments?.getFloat("rating") ?: ratingNull

        val likeMovie = binding.imageViewLike
        likeMovie.setOnClickListener {
            likeMovie.isSelected = !likeMovie.isSelected
        }

        // .кнопка просмотра. пока с тостом вместо просмотра
        binding.detailsButtonStart.setOnClickListener {
            Toast.makeText(context, getString(R.string.placeholder_start_film), Toast.LENGTH_SHORT).show()
        }

        val getMovieDetails = MovieApiClient.apiClient.getMovieDetails(
            requireArguments().getInt("id"), THE_MOVIE_DATABASE_API,
            "ru"
        )
        getMovieDetails.enqueue(object : Callback<MovieDetails> {
            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Timber.tag("TAGERROR").e(t.toString())
            }

            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                val studio = response.body()?.productionCompanies.toString()
                Timber.tag("TAGIIIIIII").e("studio -!!!!!!!!!!!!!------ $studio")
                binding.studioMovieDetails.text = studio
                binding.genreMovieDetails.text = response.body()?.genres.toString()
                binding.yearMovieDetails.text = response.body()?.releaseDate.toString()
            }
        })

        // Детали фильма. пока непонятно откуда брать
        /*binding.studioMovieDetails.text =movieTitle
        binding.genreMovieDetails.text = getString(R.string.placeholder_genre)
        binding.yearMovieDetails.text = getString(R.string.placeholder_year)*/

        // Список актёров. тоже нет в api
        val getCastActor = MovieApiClient.apiClient.getCastActor(
            requireArguments().getInt("id"), THE_MOVIE_DATABASE_API, "ru"
        )
        getCastActor.enqueue(object : Callback<ActorResponse> {
            override fun onResponse(call: Call<ActorResponse>, response: Response<ActorResponse>) {
                val actor = response.body()!!.cast

                val ActorList = actor.map {
                    ActorItem(it) { movie ->
                    }
                }.toList()

                binding.itemsContainerActor.adapter = adapter.apply { addAll(ActorList) }
            }

            override fun onFailure(call: Call<ActorResponse>, t: Throwable) {
                Timber.tag("TAGERROR").e(t.toString())
            }
        })
    }
}
