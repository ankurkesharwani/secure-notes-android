package com.ankur.securenotes.ui.common.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableTitleItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableTitleWithBodyListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableTitleWithValueListItemViewHolder

object ListViewableItemViewHolderFactory {

  fun getHolderFor(parent: ViewGroup, viewType: Int): ListViewableItemViewHolder {
    when (viewType) {
      ListItemViewHolderType.LIST_VIEWABLE_WITH_TITLE.ordinal -> {
        return getDefaultHolder(parent)
      }

      ListItemViewHolderType.LIST_VIEWABLE_WITH_TITLE_WITH_BODY.ordinal -> {
        return getWithBodyHolder(parent)
      }

      ListItemViewHolderType.LIST_VIEWABLE_WITH_TITLE_WITH_VALUE.ordinal -> {
        return getWithValueHolder(parent)
      }
    }

    return getDefaultHolder(parent)
  }

  private fun getDefaultHolder(parent: ViewGroup): ListViewableTitleItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_title, parent, false)

    return ListViewableTitleItemViewHolder(view)
  }

  private fun getWithBodyHolder(parent: ViewGroup): ListViewableTitleWithBodyListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_title_with_body, parent, false)

    return ListViewableTitleWithBodyListItemViewHolder(view)
  }

  private fun getWithValueHolder(parent: ViewGroup): ListViewableTitleWithValueListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_title_with_value, parent, false)

    return ListViewableTitleWithValueListItemViewHolder(view)
  }
}