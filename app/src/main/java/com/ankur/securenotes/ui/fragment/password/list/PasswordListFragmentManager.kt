package com.ankur.securenotes.ui.fragment.password.list

import android.content.Context
import com.ankur.securenotes.entity.PasswordEntity
import java.lang.ref.WeakReference

interface PasswordListFragmentManager {

  interface Listener {
    fun onPasswordListFetchStart(manager: PasswordListFragmentManager?)
    fun onPasswordListFetched(passwords: List<PasswordEntity>?, manager: PasswordListFragmentManager?)
    fun onPasswordListFetchFailed(errorCode: Int?, message: String?, manager: PasswordListFragmentManager?)
  }

  var passwords: List<PasswordEntity>?
  var context: Context?
  var listener: WeakReference<Listener>?

  fun fetchPasswordList()
}