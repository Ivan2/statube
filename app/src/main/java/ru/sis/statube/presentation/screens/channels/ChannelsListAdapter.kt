package ru.sis.statube.presentation.screens.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_channel.view.*
import ru.sis.statube.R
import ru.sis.statube.additional.color
import ru.sis.statube.model.Channel
import java.lang.Exception

class ChannelsListAdapter(
    channelList: List<Channel>,
    private val loadListener: (addChannels: (channelList: List<Channel>) -> Unit) -> Unit,
    private val clickListener: (channel: Channel) -> Unit,
    private val changeFavouriteListener: (channel: Channel) -> Unit
) : RecyclerView.Adapter<ChannelsListAdapter.ViewHolder>() {

    private val channelList = ArrayList<Channel>()

    init {
        this.channelList.addAll(channelList)
    }

    fun addChannels(channelList: List<Channel>) {
        this.channelList.addAll(channelList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = channelList.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == channelList.size)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_progress_bar, parent, false))
            1 -> ChannelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_channel, parent, false))
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(pos: Int)
    }

    inner class ChannelViewHolder(itemView: View) : ViewHolder(itemView) {

        override fun bind(pos: Int) {
            val channel = channelList[pos]

            itemView.vTitleTextView.text = channel.title
            itemView.vDescriptionTextView.text = channel.description
            updateFavouriteButton(itemView.vFavouriteButton, channel.isFavourite)

            itemView.vFavouriteButton.setOnClickListener {
                channel.isFavourite = !channel.isFavourite
                changeFavouriteListener(channel)
                updateFavouriteButton(itemView.vFavouriteButton, channel.isFavourite)
            }

            channel.thumbnail?.let { thumbnail ->
                Glide.with(itemView.context)
                    .load(thumbnail)
                    //.placeholder(R.drawable.product_default)
                    .circleCrop()
                    .dontAnimate()
                    .into(itemView.vImageView)
            }

            itemView.setOnClickListener {
                clickListener(channel)
            }
        }

    }

    inner class ProgressViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bind(pos: Int) {
            loadListener(::addChannels)
        }
    }

    private fun updateFavouriteButton(favouriteButton: ImageView, isFavourite: Boolean) {
        if (isFavourite) {
            favouriteButton.setImageResource(R.drawable.ic_star_black_24dp)
            favouriteButton.setColorFilter(favouriteButton.context.color(R.color.favourite))
        } else {
            favouriteButton.setImageResource(R.drawable.ic_star_border_black_24dp)
            favouriteButton.setColorFilter(favouriteButton.context.color(R.color.not_favourite))
        }
    }

}