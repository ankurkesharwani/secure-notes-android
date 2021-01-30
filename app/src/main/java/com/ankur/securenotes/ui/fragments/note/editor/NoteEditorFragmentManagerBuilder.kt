package com.ankur.securenotes.ui.fragments.note.editor

import android.content.Context
import java.lang.ref.WeakReference

class NoteEditorFragmentManagerBuilder {
    private var context: Context? = null
    private var listener: WeakReference<NoteEditorFragmentManager.Listener>? = null

    fun set(context: Context): NoteEditorFragmentManagerBuilder {
        this.context = context

        return this
    }

    fun set(listener: NoteEditorFragmentManager.Listener): NoteEditorFragmentManagerBuilder {
        this.listener = WeakReference(listener)

        return this
    }

    fun build(): NoteEditorFragmentManager {
        val manager =
            NoteEditorFragmentManagerImpl()
        manager.context = context
        manager.listener = listener

        return manager
    }
}