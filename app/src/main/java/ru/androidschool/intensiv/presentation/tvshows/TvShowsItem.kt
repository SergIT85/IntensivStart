package ru.androidschool.intensiv.presentation.tvshows

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.vo.MovieVo
import ru.androidschool.intensiv.databinding.TvShowsItemBinding

class TvShowsItem(
    private val content: MovieVo,
    private val onClick: (movie: MovieVo) -> Unit
) : BindableItem<TvShowsItemBinding>() {
    override fun bind(viewBinding: TvShowsItemBinding, position: Int) {
        viewBinding.tvShowDescription.text = content.name
        viewBinding.tvShowMovieRating.rating = content.rating
        viewBinding.tvShowItemContent.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.posterPath)
            .fit()
            .centerCrop()
            .into(viewBinding.tvShowImagePreview)
    }

    override fun getLayout(): Int = R.layout.tv_shows_item

    override fun initializeViewBinding(view: View) = TvShowsItemBinding.bind(view)
}
