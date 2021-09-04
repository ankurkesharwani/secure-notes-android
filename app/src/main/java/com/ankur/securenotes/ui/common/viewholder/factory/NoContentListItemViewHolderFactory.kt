package com.ankur.securenotes.ui.common.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.nocontent.NoContentListItemViewHolder

object NoContentListItemViewHolderFactory {

  fun getHolderFor(parent: ViewGroup, viewType: Int): NoContentListItemViewHolder {
    when (viewType) {
      ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal -> {
        return getDefaultHolder(parent)
      }
    }

    return getDefaultHolder(parent)
  }

  private fun getDefaultHolder(parent: ViewGroup): NoContentListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_no_content, parent, false)

    return NoContentListItemViewHolder(view)
  }
}