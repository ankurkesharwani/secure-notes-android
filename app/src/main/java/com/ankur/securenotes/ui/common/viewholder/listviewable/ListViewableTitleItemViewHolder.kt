package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.view.View
import android.widget.TextView
import com.ankur.securenotes.R

open class ListViewableTitleItemViewHolder(itemView: View) : ListViewableItemViewHolder(itemView), View.OnClickListener {

  private var titleTextView: TextView = itemView.findViewById(R.id.tvTitleTextView)

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(listViewable: ListViewable) {
    super.configure(listViewable)

    titleTextView.text = listViewable.value1
  }

  override fun onClick(v: View?) {
    listViewable?.let { listener?.get()?.onListViewableItemClicked(it, this) }
  }
}