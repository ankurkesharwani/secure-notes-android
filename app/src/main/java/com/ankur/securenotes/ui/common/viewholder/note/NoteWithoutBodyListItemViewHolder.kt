package com.ankur.securenotes.ui.common.viewholder.note

import android.view.View
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.util.DateUtil

class NoteWithoutBodyListItemViewHolder(itemView: View) : NoteListItemViewHolder(itemView), View.OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  override fun configure(note: NoteEntity) {
    super.configure(note)

    dateTextView.text = DateUtil.getDisplayableDate(note.updatedAt)
    titleTextView.text = note.title
  }

  override fun onClick(v: View?) {
    note?.let { listener?.get()?.onNoteItemClicked(it, this) }
  }
}