package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.databinding.MovieDetailsHeaderBinding

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
    ): View? {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        _headerBinding = MovieDetailsHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrow = headerBinding.headerToolbarDetails
        arrow.setOnClickListener {
            activity.let {
                (it as MainActivity).supportFragmentManager.popBackStack()
            }
        }

        val movieTitle = arguments?.getString("title")
        binding.nameFilmsInDetails.text = movieTitle

        val movieDescription = arguments?.getString("title")
        val textView = binding.descriptionMovieDetails
        binding.descriptionMovieDetails.text = "Тут показывает много текста так что надо это " +
                "написать, плюс текст разворачивается если на него нажать это надо обязательно " +
                "проверить. кстати надо обработать нажание на него, а пока я передал сюда " +
                "наименование фильма через arguments $movieDescription"

        var flag = true
        textView.setOnClickListener {
            if (flag) {
                textView.maxLines = Int.MAX_VALUE
                flag = false
            } else {
                textView.maxLines = 6
                flag = true
            }
        }

        binding.detailMovieRating.rating = arguments?.getFloat("rating")!!

        val likeMovie = binding.imageViewLike
        likeMovie.setOnClickListener {
            if (likeMovie.isSelected) {
                likeMovie.isSelected = false
            } else {
                likeMovie.animation
                likeMovie.isSelected = true
            }
        }

        binding.detailsButtonStart.setOnClickListener {
            Toast.makeText(context, "Начался просмотр фильма.", Toast.LENGTH_SHORT).show()
        }

        binding.studioMovieDetails.text = "Наименование студии"
        binding.genreMovieDetails.text = "Жанр фильма"
        binding.yearMovieDetails.text = "Год выпуска фильмы"

        val listActor = MockRepository.getMovies().map {
            ActorItem(it) {
                Toast.makeText(context, "Открывается описание актёра", Toast.LENGTH_SHORT).show()
            }
        }.toList()

        binding.itemsContainerActor.adapter = adapter.apply { addAll(listActor) }
    }
}
