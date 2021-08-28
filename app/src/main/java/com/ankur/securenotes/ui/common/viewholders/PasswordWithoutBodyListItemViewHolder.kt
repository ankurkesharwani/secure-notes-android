package com.ankur.securenotes.ui.common.viewholders

import android.view.View
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.utils.DateUtil

class PasswordWithoutBodyListItemViewHolder(itemView: View) : PasswordListItemViewHolder(itemView),
                                                              View.OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(password: PasswordEntity) {
    super.configure(password)

    dateTextView.text = DateUtil.getDisplayableDate(password.updatedAt)
    titleTextView.text = password.title
  }

  override fun onClick(v: View?) {
    password?.let { listener?.get()?.onPasswordItemClicked(it, this) }
  }
}