package com.ankur.securenotes.ui.common.viewholder.password

import android.view.View
import com.ankur.securenotes.entity.PasswordEntity
import com.ankur.securenotes.util.DateUtil

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