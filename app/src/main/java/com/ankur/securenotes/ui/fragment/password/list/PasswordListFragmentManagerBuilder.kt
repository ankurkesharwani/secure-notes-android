package com.ankur.securenotes.ui.fragment.password.list

import android.content.Context
import java.lang.ref.WeakReference

class PasswordListFragmentManagerBuilder {

  private var context: Context? = null
  private var listener: WeakReference<PasswordListFragmentManager.Listener>? = null

  fun set(context: Context): PasswordListFragmentManagerBuilder {
    this.context = context

    return this
  }

  fun set(listener: PasswordListFragmentManager.Listener): PasswordListFragmentManagerBuilder {
    this.listener = WeakReference(listener)

    return this
  }

  fun build(): PasswordListFragmentManager {
    val manager = PasswordListFragmentManagerImpl()
    manager.context = context
    manager.listener = listener

    return manager
  }
}