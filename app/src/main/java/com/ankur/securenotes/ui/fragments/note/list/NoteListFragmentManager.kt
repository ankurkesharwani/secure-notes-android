package com.ankur.securenotes.ui.fragments.note.list

import android.content.Context
import com.ankur.securenotes.entities.NoteEntity
import java.lang.ref.WeakReference

interface NoteListFragmentManager {

  interface Listener {
    fun onNoteListFetchStart(manager: NoteListFragmentManager?)
    fun onNoteListFetched(notes: List<NoteEntity>?, manager: NoteListFragmentManager?)
    fun onNoteListFetchFailed(errorCode: Int?, message: String?, manager: NoteListFragmentManager?)
  }

  var notes: List<NoteEntity>?
  var context: Context?
  var listener: WeakReference<Listener>?

  fun fetchNoteList()
}