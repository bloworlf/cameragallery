package io.drdroid.camera_gallery.ui.holders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import io.drdroid.camera_gallery.R

class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image: ImageView

    init {
        image = itemView.findViewById(R.id.image)
    }
}