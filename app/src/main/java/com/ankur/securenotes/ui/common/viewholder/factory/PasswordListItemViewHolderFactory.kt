package com.ankur.securenotes.ui.common.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.password.PasswordListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.password.PasswordWithoutBodyListItemViewHolder

object PasswordListItemViewHolderFactory {

  fun getHolderFor(parent: ViewGroup, viewType: Int): PasswordListItemViewHolder {
    when (viewType) {
      ListItemViewHolderType.DEFAULT_PASSWORD_LIST_ITEM.ordinal -> {
        return getDefaultHolder(parent)
      }
    }

    return getDefaultHolder(parent)
  }

  private fun getDefaultHolder(parent: ViewGroup): PasswordListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_password_default, parent, false)

    return PasswordWithoutBodyListItemViewHolder(view)
  }
}