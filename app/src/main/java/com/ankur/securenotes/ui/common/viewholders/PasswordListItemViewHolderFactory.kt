package com.ankur.securenotes.ui.common.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R

object PasswordListItemViewHolderFactory {

    @JvmStatic
    fun getHolderFor(parent: ViewGroup, viewType: Int): PasswordListItemViewHolder {
        when (viewType) {
            ListItemViewHolderType.DEFAULT_PASSWORD_LIST_ITEM.ordinal -> {
                return getDefaultHolder(parent)
            }
        }

        return getDefaultHolder(parent)
    }

    @JvmStatic
    private fun getDefaultHolder(parent: ViewGroup): PasswordListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_password_default, parent, false)

        return PasswordWithoutBodyListItemViewHolder(view)
    }
}