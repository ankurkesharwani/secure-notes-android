package com.ankur.securenotes.ui.fragments.password.editor

import android.content.Context
import java.lang.ref.WeakReference

class PasswordEditorFragmentManagerBuilder {
    private var context: Context? = null
    private var listener: WeakReference<PasswordEditorFragmentManager.Listener>? = null

    fun set(
        context: Context
    ): PasswordEditorFragmentManagerBuilder {
        this.context = context

        return this
    }

    fun set(
        listener: PasswordEditorFragmentManager.Listener
    ): PasswordEditorFragmentManagerBuilder {
        this.listener = WeakReference(listener)

        return this
    }

    fun build(): PasswordEditorFragmentManager {
        val manager = PasswordEditorFragmentManagerImpl()
        manager.context = context
        manager.listener = listener

        return manager
    }
}