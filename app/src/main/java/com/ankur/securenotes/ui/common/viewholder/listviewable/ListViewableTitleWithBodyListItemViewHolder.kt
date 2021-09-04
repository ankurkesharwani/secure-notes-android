package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.view.View
import android.widget.TextView
import com.ankur.securenotes.R

class ListViewableTitleWithBodyListItemViewHolder(itemView: View) : ListViewableTitleItemViewHolder(itemView) {

  private var bodyTextView: TextView = itemView.findViewById(R.id.tvBodyTextView)

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(listViewable: ListViewable) {
    super.configure(listViewable)

    bodyTextView.text = listViewable.value2
  }
}