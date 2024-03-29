package com.ankur.securenotes.ui.common.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.PasswordEntity
import java.lang.ref.WeakReference

abstract class PasswordListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  interface Listener {
    fun onPasswordItemClicked(password: PasswordEntity, viewHolder: RecyclerView.ViewHolder)
  }

  open var titleTextView: TextView = itemView.findViewById(R.id.tvTitleTextView)
  open var dateTextView: TextView = itemView.findViewById(R.id.tvDateTextView)

  open var listener: WeakReference<Listener>? = null
  var password: PasswordEntity? = null

  fun setListener(listener: Listener) {
    this.listener = WeakReference(listener)
  }

  open fun configure(password: PasswordEntity) {
    this.password = password
  }
}