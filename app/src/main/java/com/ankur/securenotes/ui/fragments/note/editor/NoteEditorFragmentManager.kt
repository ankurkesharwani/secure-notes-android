package com.ankur.securenotes.ui.fragments.note.editor

import android.content.Context
import com.ankur.securenotes.entities.NoteEntity
import java.lang.ref.WeakReference

interface NoteEditorFragmentManager {
    interface Listener {
        fun onNoteSavingStarted(
            note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?
        )

        fun onNoteSaved(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?)

        fun onNoteSavingFailed(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?)

        fun onNoteDeletionStarted(
            note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?
        )

        fun onNoteDeleted(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?)

        fun onNoteDeletionFailed(
            note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?
        )
    }

    var note: NoteEntity?
    var context: Context?
    var listener: WeakReference<Listener>?

    fun saveNote()
    fun deleteNote()
}