package ru.androidschool.intensiv.presentation.movie_details

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.ActorCast
import ru.androidschool.intensiv.databinding.ItemAvatarActorBinding

class ActorItem(
    private val content: ActorCast,
    private val onClick: (movie: ActorCast) -> Unit
) : BindableItem<ItemAvatarActorBinding>() {
    override fun bind(viewBinding: ItemAvatarActorBinding, position: Int) {
        viewBinding.description.text = content.name
        viewBinding.avatarActor.setOnClickListener {
            onClick.invoke(content)
        }
        Picasso.get()
            .load(content.profilePath)
            .fit()
            .centerCrop()
            .into(viewBinding.avatarActor)
    }

    override fun getLayout(): Int = R.layout.item_avatar_actor

    override fun initializeViewBinding(view: View) = ItemAvatarActorBinding.bind(view)
}
