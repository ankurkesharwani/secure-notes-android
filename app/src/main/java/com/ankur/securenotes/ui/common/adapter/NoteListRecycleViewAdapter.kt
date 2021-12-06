package com.ankur.securenotes.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.ui.common.viewholder.ListItemViewHolderType
import com.ankur.securenotes.ui.common.viewholder.factory.NoContentListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.factory.NoteListItemViewHolderFactory
import com.ankur.securenotes.ui.common.viewholder.note.NoteListItemViewHolder

class NoteListRecycleViewAdapter(var listener: NoteListItemViewHolder.Listener?) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var notes = listOf<NoteEntity>()
  private var countItems: Int = 0
  private var hasItemsToShow: Boolean = false

  fun updateNotes(notes: List<NoteEntity>?) {
    notes?.let {
      this.notes = it
    }
    countItems = this.notes.count()
    hasItemsToShow = countItems > 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (hasItemsToShow) {
      val holder = NoteListItemViewHolderFactory.getHolderFor(parent, viewType)
      listener?.let { holder.setListener(it) }

      return holder
    } else {
      NoContentListItemViewHolderFactory.getHolderFor(parent, viewType)
    }
  }

  override fun getItemCount(): Int {
    return if (hasItemsToShow) {
      countItems
    } else {
      1 // This one is for the empty view.
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is NoteListItemViewHolder) {
      holder.configure(notes[position])
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (hasItemsToShow) {
      ListItemViewHolderType.NOTE_WITHOUT_BODY_LIST_ITEM.ordinal
    } else {
      ListItemViewHolderType.DEFAULT_NO_CONTENT_LIST_ITEM.ordinal
    }
  }
}