package com.ankur.securenotes.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.factory.NoContentListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.factory.PasswordSectionListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableItemViewHolder

class PasswordViewerRecyclerViewAdapter(listener: ListViewableItemViewHolder.Listener?) :
  ListViewableRecyclerViewAdapter(
    listener
  ) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (hasItemsToShow) {
      val holder = PasswordSectionListItemViewHolderFactory.getHolderFor(parent, viewType)
      listener?.let { holder.setListener(it) }

      return holder
    } else {
      NoContentListItemViewHolderFactory.getHolderFor(parent, viewType)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (hasItemsToShow) {
      if (position == itemCount - 1) {
        ListItemViewHolderType.PASSWORD_SECURE_SECTION.ordinal
      } else {
        ListItemViewHolderType.PASSWORD_SECTION.ordinal
      }
    } else {
      ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal
    }
  }
}