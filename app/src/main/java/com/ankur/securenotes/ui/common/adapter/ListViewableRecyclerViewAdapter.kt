package com.ankur.securenotes.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.factory.ListViewableItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.factory.NoContentListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewable
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableItemViewHolder

class ListViewableRecyclerViewAdapter(var listener: ListViewableItemViewHolder.Listener?) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items = listOf<ListViewable>()
  private var countItems: Int = 0
  private var hasItemsToShow: Boolean = false

  fun updateItems(items: List<ListViewable>?) {
    items?.let {
      this.items = it
    }
    countItems = this.items.count()
    hasItemsToShow = countItems > 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (hasItemsToShow) {
      val holder = ListViewableItemViewHolderFactory.getHolderFor(parent, viewType)
      listener?.let { holder.setListener(it) }

      return holder
    } else {
      NoContentListItemViewHolderFactory.getHolderFor(parent, viewType)
    }
  }

  override fun getItemCount(): Int {
    return if (hasItemsToShow) {
      countItems
    } else {
      1 // This one is for the empty view.
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ListViewableItemViewHolder) {
      holder.configure(items[position])
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (hasItemsToShow) {
      ListItemViewHolderType.LIST_VIEWABLE_WITH_TITLE.ordinal
    } else {
      ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal
    }
  }
}