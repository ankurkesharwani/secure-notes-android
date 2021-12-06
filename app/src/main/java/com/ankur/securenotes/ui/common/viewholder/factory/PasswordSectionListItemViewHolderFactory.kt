package com.ankur.securenotes.ui.common.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewableItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.PasswordSectionListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.PasswordSecureSectionListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.listviewable.TitleListItemViewHolder

object PasswordSectionListItemViewHolderFactory {
  fun getHolderFor(parent: ViewGroup, viewType: Int): ListViewableItemViewHolder {
    when (viewType) {
      ListItemViewHolderType.PASSWORD_SECTION.ordinal -> {
        return getPasswordSectionHolder(parent)
      }

      ListItemViewHolderType.PASSWORD_SECURE_SECTION.ordinal -> {
        return getPasswordSecureSectionHolder(parent)
      }
    }

    return getDefaultHolder(parent)
  }

  private fun getDefaultHolder(parent: ViewGroup): TitleListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_password_value_section, parent, false)

    return PasswordSectionListItemViewHolder(view)
  }

  private fun getPasswordSectionHolder(parent: ViewGroup): TitleListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_password_value_section, parent, false)

    return PasswordSectionListItemViewHolder(view)
  }

  private fun getPasswordSecureSectionHolder(parent: ViewGroup): TitleListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_password_secure_section, parent, false)

    return PasswordSecureSectionListItemViewHolder(view)
  }
}