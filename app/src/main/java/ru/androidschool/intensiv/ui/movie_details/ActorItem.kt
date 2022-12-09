package ru.androidschool.intensiv.ui.movie_details

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.ItemAvatarActorBinding

class ActorItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
) : BindableItem<ItemAvatarActorBinding>() {
    override fun bind(viewBinding: ItemAvatarActorBinding, position: Int) {
        viewBinding.description.text = content.title
        viewBinding.avatarActor.setOnClickListener {
            onClick.invoke(content)
        }
        Picasso.get()
            .load("https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtO" +
                    "TQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
            .into(viewBinding.avatarActor)
    }

    override fun getLayout(): Int = R.layout.item_avatar_actor

    override fun initializeViewBinding(view: View) = ItemAvatarActorBinding.bind(view)
}
