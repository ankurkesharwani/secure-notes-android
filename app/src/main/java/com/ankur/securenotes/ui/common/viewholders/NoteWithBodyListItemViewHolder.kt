package com.ankur.securenotes.ui.common.viewholders

import android.view.View
import android.widget.TextView
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.utils.DateUtil

class NoteWithBodyListItemViewHolder(itemView: View) : NoteListItemViewHolder(itemView),
                                                       View.OnClickListener {

    private var bodyTextView: TextView = itemView.findViewById(R.id.tvBodyTextView)

    init {
        itemView.setOnClickListener(this)
    }

    override fun configure(note: NoteEntity) {
        super.configure(note)

        dateTextView.text = DateUtil.getDisplayableDate(note.updatedAt)
        titleTextView.text = note.title
        bodyTextView.text = note.body
    }

    override fun onClick(v: View?) {
        note?.let { listener?.get()?.onNoteItemClicked(it, this) }
    }
}