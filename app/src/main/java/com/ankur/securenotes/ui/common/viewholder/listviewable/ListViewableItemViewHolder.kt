package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class ListViewableItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  interface Listener {
    fun onListViewableItemClicked(item: ListViewable, viewHolder: RecyclerView.ViewHolder)
  }

  open var listener: WeakReference<Listener>? = null
  var listViewable: ListViewable? = null

  fun setListener(listener: Listener) {
    this.listener = WeakReference(listener)
  }

  open fun configure(listViewable: ListViewable) {
    this.listViewable = listViewable
  }
}