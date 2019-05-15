package ru.sis.statube.presentation.screens.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_channel.view.*
import ru.sis.statube.R
import ru.sis.statube.additional.color
import ru.sis.statube.additional.loadChannelThumbnail
import ru.sis.statube.model.Channel

class ChannelsListAdapter(
    private val loadListener: () -> Unit,
    private val clickListener: (channel: Channel) -> Unit,
    private val changeFavouriteListener: (channel: Channel) -> Unit
) : RecyclerView.Adapter<ChannelsListAdapter.ViewHolder>() {

    private val channelList = ArrayList<Channel>()
    private var toLoadMore = false
    private var showLoadingMore = false

    fun addChannels(channelList: List<Channel>, withMore: Boolean) {
        this.toLoadMore = withMore
        this.showLoadingMore = withMore
        this.channelList.addAll(channelList)
        notifyDataSetChanged()
    }

    fun clear() {
        this.toLoadMore = false
        this.showLoadingMore = false
        this.channelList.clear()
        notifyDataSetChanged()
    }

    fun addChannel(channel: Channel) {
        channelList.add(channel)
        notifyItemInserted(channelList.lastIndex)
    }

    fun removeChannel(channel: Channel) {
        for (i in 0 until channelList.size) {
            if (channelList[i].id == channel.id) {
                channelList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    fun getChannels() = ArrayList(channelList)

    override fun getItemCount(): Int = channelList.size + if (showLoadingMore) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (position == channelList.size) 0 else 1
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
            itemView.vImageView.loadChannelThumbnail(channel.thumbnail)
            updateFavouriteButton(itemView.vFavouriteButton, channel.isFavourite)

            itemView.vFavouriteButton.setOnClickListener {
                channel.isFavourite = !channel.isFavourite
                changeFavouriteListener(channel)
                updateFavouriteButton(itemView.vFavouriteButton, channel.isFavourite)
            }

            itemView.setOnClickListener {
                clickListener(channel)
            }
        }

    }

    inner class ProgressViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bind(pos: Int) {
            if (toLoadMore) {
                toLoadMore = false
                loadListener()
            }
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