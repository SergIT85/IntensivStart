package ru.androidschool.intensiv.ui.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MovieDatabase
import ru.androidschool.intensiv.data.MovieEntity
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.databinding.MovieDetailsHeaderBinding
import ru.androidschool.intensiv.extension.extCompletable
import ru.androidschool.intensiv.extension.extSingle
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

const val ID = "id"
const val TITLE = "title"
const val POSTERPATH = "posterPath"
const val OVERVIEW = "overview"
const val RATING = "rating"

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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Постер фильма в хэдере
        Picasso.get()
            .load(requireArguments().getString(POSTERPATH))
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
        val movieTitle = requireArguments().getString(TITLE)

        binding.nameFilmsInDetails.text = movieTitle

        // Устанавливаем описание
        val movieDescription = requireArguments().getString(OVERVIEW)
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
        binding.detailMovieRating.rating = arguments?.getFloat(RATING) ?: ratingNull

        // БД
        val databaseMovie = MovieDatabase.get(requireContext())
        // Кнопка лайка
        val likeMovie = binding.imageViewLike

        databaseMovie.movieDao()
            .exists(requireArguments().getInt(ID).toLong())
            .extSingle()
            .subscribe({ equal ->
                likeMovie.isChecked = equal
            }, {
                    error -> Timber.tag("TAGERROR").e(error.toString())
            })

        likeMovie.setOnClickListener {
            if (!likeMovie.isChecked) {
                likeMovie.isChecked = false
                databaseMovie.movieDao().deleteById(requireArguments().getInt(ID).toLong())
                    .extCompletable()
                    .subscribe({ databaseMovie.movieDao()
                        .deleteById(requireArguments().getInt(ID).toLong())
                    }, {
                            error -> Timber.tag("TAGERROR").e(error.toString())
                    })
            } else {
                likeMovie.isChecked = true
                val movieEntity = MovieEntity(
                    requireArguments().getInt(ID).toLong(),
                    requireArguments().getString(TITLE),
                    requireArguments().getString(POSTERPATH),
                    requireArguments().getString(OVERVIEW),
                    requireArguments().getFloat(RATING)
                    )
                databaseMovie.movieDao().save(movieEntity)
                    .extCompletable()
                    .subscribe({ databaseMovie.movieDao().save(movieEntity)
                    }, {
                            error -> Timber.tag("TAGERROR").e(error.toString())
                    })
            }
        }

        // .кнопка просмотра. пока с тостом вместо просмотра
        binding.detailsButtonStart.setOnClickListener {
            Toast.makeText(context, getString(R.string.placeholder_start_film), Toast.LENGTH_SHORT).show()
        }

        val getMovieDetails = MovieApiClient.apiClient.getMovieDetails(
            requireArguments().getInt(ID)
        )
        getMovieDetails
            .extSingle()
            .subscribe({ detail ->
                val studio = detail.productionCompanies
                val genre = detail.genres
                val releaseDate = detail.releaseDate

                if (studio.isNotEmpty()) {
                    binding.studioMovieDetails.text = studio[0].name.toString()
                } else {
                    binding.studioMovieDetails.text = R.string.placeholder_data.toString()
                }

                if (genre.isNotEmpty()) {
                    binding.genreMovieDetails.text = genre[0].name.toString()
                } else {
                    binding.genreMovieDetails.text = R.string.placeholder_data.toString()
                }

                binding.yearMovieDetails.text = releaseDate.toString()
            }, { error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
            })

        // Список актёров.
        val getCastActor = MovieApiClient.apiClient.getCastActor(
            requireArguments().getInt(ID))
        getCastActor
            .extSingle()
            .subscribe({ actor ->
                val movieActor = actor.cast

                val actorList = movieActor.map {
                    ActorItem(it) { }
                }.toList()
                binding.itemsContainerActor.adapter = adapter.apply { addAll(actorList) }
                binding.progressBar.visibility = View.INVISIBLE
            }, { error ->
                // Логируем ошибку
                Timber.tag("TAGERROR").e(error.toString())
                binding.progressBar.visibility = View.INVISIBLE
            })
    }
}
