package com.ankur.securenotes.ui.fragments.password.editor

import android.content.Context
import com.ankur.securenotes.entities.PasswordEntity
import java.lang.ref.WeakReference

interface PasswordEditorFragmentManager {

  interface Listener {
    fun onPasswordSavingStarted(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )

    fun onPasswordSaved(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )

    fun onPasswordSavingFailed(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )

    fun onPasswordDeletionStarted(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )

    fun onPasswordDeleted(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )

    fun onPasswordDeletionFailed(
      password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
    )
  }

  var password: PasswordEntity?
  var context: Context?
  var listener: WeakReference<Listener>?

  fun savePassword()
  fun deletePassword()
}