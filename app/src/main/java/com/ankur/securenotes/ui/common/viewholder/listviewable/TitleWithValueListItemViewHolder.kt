package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.view.View
import android.widget.TextView
import com.ankur.securenotes.R

open class TitleWithValueListItemViewHolder(itemView: View) : TitleListItemViewHolder(itemView) {

  var valueTextView: TextView = itemView.findViewById(R.id.valueTextView)

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(listViewable: ListViewable) {
    super.configure(listViewable)

    valueTextView.text = listViewable.value2
  }
}