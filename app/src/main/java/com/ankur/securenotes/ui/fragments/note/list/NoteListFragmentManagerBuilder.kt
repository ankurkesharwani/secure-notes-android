package com.ankur.securenotes.ui.fragments.note.list

import android.content.Context
import java.lang.ref.WeakReference

class NoteListFragmentManagerBuilder {
    private var context: Context? = null
    private var listener: WeakReference<NoteListFragmentManager.Listener>? = null

    fun set(context: Context): NoteListFragmentManagerBuilder {
        this.context = context

        return this
    }

    fun set(listener: NoteListFragmentManager.Listener): NoteListFragmentManagerBuilder {
        this.listener = WeakReference(listener)

        return this
    }

    fun build(): NoteListFragmentManager {
        val manager =
            NoteListFragmentManagerImpl()
        manager.context = context
        manager.listener = listener

        return manager
    }
}