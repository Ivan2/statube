package ru.sis.statube.presentation.screens.videos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_video.view.*
import ru.sis.statube.R
import ru.sis.statube.additional.format
import ru.sis.statube.additional.formatDuration
import ru.sis.statube.additional.loadVideoThumbnail
import ru.sis.statube.model.Video

class VideosListAdapter(
    private val clickListener: (video: Video) -> Unit
) : RecyclerView.Adapter<VideosListAdapter.ViewHolder>() {

    private var videoList: List<Video> = emptyList()

    fun updateVideos(videoList: List<Video>) {
        this.videoList = videoList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = videoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_video, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            val video = videoList[pos]

            itemView.apply {
                vTitleTextView.text = video.title
                vImageView.loadVideoThumbnail(video.thumbnail)

                vPublishedAtTextView.text = video.publishedAt.toString("dd.MM.yyyy HH:mm:ss")
                vDurationTextView.text = video.duration?.formatDuration() ?: "?"
                vViewCountTextView.text = video.viewCount?.format() ?: "?"
                vCommentCountTextView.text = video.commentCount?.format() ?: "?"

                vLikeCountTextView.text = video.likeCount?.format() ?: "?"
                vDislikeCountTextView.text = video.dislikeCount?.format() ?: "?"

                val likeCount = (video.likeCount ?: 0).toFloat()
                val dislikeCount = (video.dislikeCount ?: 0).toFloat()
                var sum = likeCount + dislikeCount
                if (sum == 0f)
                    sum = 1f
                val likeLayoutParams = vLikeView.layoutParams as LinearLayout.LayoutParams
                val dislikeLayoutParams = vDislikeView.layoutParams as LinearLayout.LayoutParams
                likeLayoutParams.weight = likeCount / sum
                dislikeLayoutParams.weight = dislikeCount / sum
                vLikeView.layoutParams = likeLayoutParams
                vDislikeView.layoutParams = dislikeLayoutParams

                setOnClickListener {
                    clickListener(video)
                }
            }
        }

    }

}