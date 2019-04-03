package ru.sis.statube.presentation.screens.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_channel.view.*
import ru.sis.statube.R
import ru.sis.statube.model.Channel
import java.lang.Exception

class ChannelsListAdapter(
    channelList: List<Channel>,
    private val loadListener: (addChannels: (channelList: List<Channel>) -> Unit) -> Unit,
    private val clickListener: (channel: Channel) -> Unit
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

            val img = channel.thumbnails?.let { thumbnails ->
                thumbnails.medium?.url ?: thumbnails.high?.url ?: thumbnails.default?.url
            }

            img?.let {
                Glide.with(itemView.context)
                    .load(img)
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

}