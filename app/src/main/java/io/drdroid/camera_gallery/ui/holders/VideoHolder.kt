package io.drdroid.camera_gallery.ui.holders

import android.view.View
import android.widget.TextView
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import io.drdroid.camera_gallery.R

class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val video: PlayerView
    val url: TextView

    init {
        video = itemView.findViewById(R.id.video)
        url = itemView.findViewById(R.id.urlHolder)
    }
}