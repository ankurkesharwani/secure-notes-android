package com.ankur.securenotes.ui.fragments.password.viewer

import android.content.Context

class PasswordViewerFragmentManagerBuilder {
  private var context: Context? = null

  fun set(context: Context): PasswordViewerFragmentManagerBuilder {
    this.context = context

    return this
  }

  fun build(): PasswordViewerFragmentManager {
    val manager = PasswordViewerFragmentManagerImpl()
    manager.context = context

    return manager
  }
}