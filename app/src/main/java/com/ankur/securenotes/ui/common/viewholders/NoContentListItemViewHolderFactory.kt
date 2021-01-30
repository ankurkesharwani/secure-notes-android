package com.ankur.securenotes.ui.common.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R

object NoContentListItemViewHolderFactory {
    @JvmStatic
    fun getHolderFor(
        parent: ViewGroup,
        viewType: Int
    ): NoContentListItemViewHolder {
        when (viewType) {
            ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal -> {
                return getDefaultHolder(
                    parent
                )
            }
        }

        return getDefaultHolder(
            parent
        )
    }

    @JvmStatic
    private fun getDefaultHolder(parent: ViewGroup): NoContentListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.list_item_no_content,
                parent,
                false
            )

        return NoContentListItemViewHolder(
            view
        )
    }
}