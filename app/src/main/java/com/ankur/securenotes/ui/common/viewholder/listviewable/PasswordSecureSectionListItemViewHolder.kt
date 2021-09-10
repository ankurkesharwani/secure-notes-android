package com.ankur.securenotes.ui.common.viewholder.listviewable

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import com.ankur.securenotes.R

class PasswordSecureSectionListItemViewHolder(itemView: View) : TitleWithValueListItemViewHolder(itemView),
                                                                View.OnClickListener {
  private val bCopyButton = itemView.findViewById<Button>(R.id.copyButton)
  private val bRevealPasswordButton = itemView.findViewById<Button>(R.id.revealPasswordButton)
  private var revealPassword = false

  override fun configure(listViewable: ListViewable) {
    super.configure(listViewable)

    bCopyButton.setOnClickListener(this)
    bRevealPasswordButton.setOnClickListener(this)
  }

  private fun copyPassword() {
    // Todo: Add copy functionality
  }

  private fun revealPassword() {
    if (revealPassword) {
      valueTextView.transformationMethod = PasswordTransformationMethod.getInstance()
      bRevealPasswordButton.text = itemView.context.getString(R.string.password_editor_button_title_reveal_password);
      revealPassword = false
    } else {
      valueTextView.transformationMethod = HideReturnsTransformationMethod.getInstance()
      bRevealPasswordButton.text = itemView.context.getString(R.string.password_editor_button_title_hide_password);
      revealPassword = true
    }
  }

  override fun onClick(view: View?) {
    when (view) {
      bCopyButton -> {
        copyPassword()
      }
      bRevealPasswordButton -> {
        revealPassword()
      }
    }
  }
}