package io.drdroid.camera_gallery.ui.adapters

import android.content.ContentResolver.MimeTypeInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.drdroid.camera_gallery.R
import io.drdroid.camera_gallery.data.models.GalleryItem
import io.drdroid.camera_gallery.ui.holders.GalleryItemHolder
import io.drdroid.camera_gallery.utils.DiskUtils

class GalleryFolderAdapter(
    private val context: Context,
    var list: List<GalleryItem>,
    val controller: NavController
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.model_gallery_item, parent, false)
        return GalleryItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(h0: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        val holder = h0 as GalleryItemHolder

        val pos = position

        //        println(folder.files.last())
        if (item.mimeType == "video/mp4") {
            holder.play.visibility = View.VISIBLE
        } else {
            holder.play.visibility = View.GONE
        }
        holder.title.text = item.name
        holder.count.text = DiskUtils(item.size.toLong()).toString()
        Glide.with(context)
            .load(item.path)
            .error(R.mipmap.ic_launcher)
            .thumbnail(Glide.with(context).load(R.drawable.loading).fitCenter())
            .into(holder.thumbnail)

        holder.itemView.setOnClickListener {
            controller.navigate(
                R.id.folder_to_media,
                bundleOf(
                    "position" to pos,
                    "list" to Gson().toJson(list)
                )
            )
        }
    }
}