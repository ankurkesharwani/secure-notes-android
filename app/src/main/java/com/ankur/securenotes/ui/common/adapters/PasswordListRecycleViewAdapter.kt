package com.ankur.securenotes.ui.common.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.ui.common.viewholders.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholders.NoContentListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholders.PasswordListItemViewHolder
import com.ankur.securenotes.ui.common.viewholders.PasswordListItemViewHolderFactory

class PasswordListRecycleViewAdapter(var listener: PasswordListItemViewHolder.Listener?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var passwords = listOf<PasswordEntity>()
    private var countItems: Int = 0
    private var hasItemsToShow: Boolean = false

    fun updatePasswords(passwords: List<PasswordEntity>?) {
        passwords?.let {
            this.passwords = it
        }
        countItems = this.passwords.count()
        hasItemsToShow = countItems > 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (hasItemsToShow) {
            val holder = PasswordListItemViewHolderFactory.getHolderFor(parent, viewType)
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
        if (holder is PasswordListItemViewHolder) {
            holder.configure(passwords[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasItemsToShow) {
            ListItemViewHolderType.DEFAULT_PASSWORD_LIST_ITEM.ordinal
        } else {
            ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal
        }
    }
}