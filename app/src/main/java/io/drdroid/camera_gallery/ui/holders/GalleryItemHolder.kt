package io.drdroid.camera_gallery.ui.holders

import android.view.ContextMenu
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import io.drdroid.camera_gallery.R

class GalleryItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnCreateContextMenuListener {

    val thumbnail: ShapeableImageView
    val play: ImageButton
    val title: TextView
    val count: TextView

    init {
        thumbnail = itemView.findViewById(R.id.thumbnail)
        play = itemView.findViewById(R.id.play)
        title = itemView.findViewById(R.id.title)
        count = itemView.findViewById(R.id.count)

        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        //
    }

}
