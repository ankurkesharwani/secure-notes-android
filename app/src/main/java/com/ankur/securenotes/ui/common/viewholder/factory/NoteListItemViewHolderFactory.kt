package com.ankur.securenotes.ui.common.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.note.NoteListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.note.NoteWithBodyListItemViewHolder
import com.ankur.securenotes.ui.common.viewholder.note.NoteWithoutBodyListItemViewHolder

object NoteListItemViewHolderFactory {

  fun getHolderFor(parent: ViewGroup, viewType: Int): NoteListItemViewHolder {
    when (viewType) {
      ListItemViewHolderType.DEFAULT_NOTE_LIST_ITEM.ordinal -> {
        return getDefaultHolder(parent)
      }

      ListItemViewHolderType.NOTE_WITH_BODY_LIST_ITEM.ordinal -> {
        return getWithBodyHolder(parent)
      }

      ListItemViewHolderType.NOTE_WITHOUT_BODY_LIST_ITEM.ordinal -> {
        return getWithoutBodyHolder(parent)
      }
    }

    return getDefaultHolder(parent)
  }

  private fun getDefaultHolder(parent: ViewGroup): NoteListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_note_no_body, parent, false)

    return NoteWithoutBodyListItemViewHolder(view)
  }

  private fun getWithBodyHolder(parent: ViewGroup): NoteListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_note_with_body, parent, false)

    return NoteWithBodyListItemViewHolder(view)
  }

  private fun getWithoutBodyHolder(parent: ViewGroup): NoteListItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_note_no_body, parent, false)

    return NoteWithoutBodyListItemViewHolder(view)
  }
}