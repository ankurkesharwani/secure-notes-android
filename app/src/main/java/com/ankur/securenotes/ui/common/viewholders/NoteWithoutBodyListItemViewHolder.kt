package com.ankur.securenotes.ui.common.viewholders

import android.view.View
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.utils.DateUtil

class NoteWithoutBodyListItemViewHolder(itemView: View): NoteListItemViewHolder(itemView), View.OnClickListener {
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