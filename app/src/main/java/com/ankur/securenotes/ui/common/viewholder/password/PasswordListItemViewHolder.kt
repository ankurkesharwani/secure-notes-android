package com.ankur.securenotes.ui.common.viewholder.password

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.R
import com.ankur.securenotes.entity.PasswordEntity
import java.lang.ref.WeakReference

abstract class PasswordListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  interface Listener {
    fun onPasswordItemClicked(password: PasswordEntity, viewHolder: RecyclerView.ViewHolder)
  }

  open var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
  open var dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

  open var listener: WeakReference<Listener>? = null
  var password: PasswordEntity? = null

  fun setListener(listener: Listener) {
    this.listener = WeakReference(listener)
  }

  open fun configure(password: PasswordEntity) {
    this.password = password
  }
}