package io.drdroid.camera_gallery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.drdroid.camera_gallery.R
import io.drdroid.camera_gallery.data.models.Folder
import io.drdroid.camera_gallery.ui.holders.GalleryItemHolder
import io.drdroid.camera_gallery.utils.Common
import io.drdroid.camera_gallery.utils.Utils.hideKeyboard


class GalleryAdapter(
    private val context: Context,
    var list: MutableList<Folder>,
    var controller: NavController
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filteredList: List<Folder> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.model_gallery_folder, parent, false)
        return GalleryItemHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }

    override fun onBindViewHolder(h0: RecyclerView.ViewHolder, position: Int) {
        val folder = filteredList[position]
        val holder = h0 as GalleryItemHolder

//        val pos = position

//        println(folder.files.last())
        holder.title.text = folder.name
        holder.count.text = folder.files.size.toString()
        Glide.with(context)
            .load(folder.files.last().path)
            .error(R.mipmap.ic_launcher)
            .thumbnail(Glide.with(context).load(R.drawable.loading).fitCenter())
            .into(holder.thumbnail)

//        Glide.with(context)
//            .asBitmap()
//            .load(folder.files.last())
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    holder.thumbnail.setImageBitmap(resource)
//                    holder.thumbnail.buildDrawingCache()
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//            })

        holder.itemView.setOnClickListener {
            it.hideKeyboard()
            controller.navigate(
                R.id.gallery_to_folder,
                bundleOf(
                    "title" to (folder.name ?: ""),
                    "list" to Gson().toJson(folder.files)
                )
            )
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.count = list.size
                    filterResults.values = list
                } else {
//                    val results: ArrayList<PeopleModel> = arrayListOf()
                    val str: String = constraint.toString()
                    val resultList = list.filter {
                        it.name?.lowercase()?.contains(str.lowercase()) ?: false ||
                                it.files.any { f -> f.name.lowercase().contains(str) }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Folder>
                notifyDataSetChanged()
            }
        }
    }
}