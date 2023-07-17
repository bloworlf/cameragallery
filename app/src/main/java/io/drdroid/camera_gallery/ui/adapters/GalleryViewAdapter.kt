package io.drdroid.camera_gallery.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.drdroid.camera_gallery.R
import io.drdroid.camera_gallery.data.models.GalleryItem
import io.drdroid.camera_gallery.ui.holders.ImageHolder
import io.drdroid.camera_gallery.ui.holders.VideoHolder

class GalleryViewAdapter(private val context: Context, private val list: List<GalleryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var seekTo: ArrayList<Int> = ArrayList(list.size)
    private lateinit var player: ExoPlayer

    init {
        seekTo.fill(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view: View
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            2 -> {
                //video
                view = layoutInflater.inflate(R.layout.model_gallery_view_video, parent, false)
                viewHolder = VideoHolder(view)
            }

            else -> {
                //image
                view = layoutInflater.inflate(R.layout.model_gallery_view_image, parent, false)
                viewHolder = ImageHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].mimeType) {
            "video/mp4" -> {
                2
            }

            else -> {
                1
            }
        }
    }

    override fun onBindViewHolder(h0: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (getItemViewType(position)) {
            2 -> {
                val holder = h0 as VideoHolder
                holder.url.text = item.path

                holder.video.player = updatePlayer(item.path)

//                // Build the media item.
//                val mediaItem = MediaItem.fromUri(item.path)
//                // Set the media item to be played.
//                player.setMediaItem(mediaItem)
//                // Prepare the player.
//                player.prepare()
////                // Start the playback.
////                player.play()
//
//                // Add a listener to receive events from the player.
//                player.addListener(object : Player.Listener {
//                    override fun onPlayerError(error: PlaybackException) {
////                        super.onPlayerError(error)
//                        Toast.makeText(
//                            context,
//                            "Error while playing video: ${error.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })

//                // creating an object of media controller class
//                val mediaControls = MediaController(context)
//
//                // set the anchor view for the video view
//                mediaControls.setAnchorView(holder.video)
//
//                // set the media controller for video view
//                holder.video.setMediaController(mediaControls)
//
//                holder.video.setVideoURI(
//                    Uri.parse(item.path)
//                )
//
//                holder.video.requestFocus()
//
//                // display a toast message if any
//                // error occurs while playing the video
//                holder.video.setOnErrorListener { mp, what, extra ->
//                    Toast.makeText(
//                        context, "An Error Occurred " +
//                                "While Playing Video !!!", Toast.LENGTH_LONG
//                    ).show()
//                    false
//                }
//
//                holder.video


            }

            else -> {
                val holder = h0 as ImageHolder
                Glide.with(context)
                    .load(item.path)
                    .into(holder.image)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == 2) {
//            (holder as VideoHolder).video.player?.release()
            releasePlayer()
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == 2) {
            (holder as VideoHolder).video.player = updatePlayer(
                (holder as VideoHolder).url.text.toString()
            )
//            if (holder.layoutPosition == startPage) {
//                this.playerView = (holder as VideoHolder).video
//            }
        }
    }

    fun updatePlayer(link: String): ExoPlayer {
        player = ExoPlayer.Builder(context)
            .build()
        val playerListener: Player.Listener = object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
//                        Player.Listener.super.onPlayerError(error);
                if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                    // Re-initialize player at the current live window default position.
                    player.seekToDefaultPosition()
                    player.prepare()
                } else {
                    // Handle other errors.
                }
            }
        }
        player.addListener(playerListener)
        //        }
        val builder: MediaItem.Builder = MediaItem.Builder()
        builder.setUri(Uri.parse(link))

        val item = builder.build()
        player.setMediaItem(item)
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE

        return player
    }

    private fun stopVideo() {
        player.pause()
    }

    fun releasePlayer() {
        if (!this::player.isInitialized) {
            return
        }
        stopVideo()
        player.release()
    }
}