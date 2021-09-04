package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.view.View
import android.widget.TextView
import com.ankur.securenotes.R

class ListViewableTitleWithValueListItemViewHolder(itemView: View) : ListViewableTitleItemViewHolder(itemView) {

  private var valueTextView: TextView = itemView.findViewById(R.id.tvValueTextView)

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(listViewable: ListViewable) {
    super.configure(listViewable)

    valueTextView.text = listViewable.value2
  }
}