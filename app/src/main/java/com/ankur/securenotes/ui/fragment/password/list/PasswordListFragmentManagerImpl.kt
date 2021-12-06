package com.ankur.securenotes.ui.fragment.password.list

import android.content.Context
import com.ankur.securenotes.entity.PasswordEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.task.GetAllPasswordsTask
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import java.lang.ref.WeakReference

class PasswordListFragmentManagerImpl : PasswordListFragmentManager, SerialTaskExecutor.Listener {

  override var passwords: List<PasswordEntity>? = null
  override var context: Context? = null
  override var listener: WeakReference<PasswordListFragmentManager.Listener>? = null

  override fun fetchPasswordList() {
    context?.let {
      Shared.serialTaskExecutor?.exec(
        GetAllPasswordsTask(Shared.getReadableDatabase(it)), this
      )
    }
  }

  override fun onTaskStarted(task: Task) {
    if (task is GetAllPasswordsTask) {
      listener?.get()?.onPasswordListFetchStart(this)
    }
  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is GetAllPasswordsTask -> {
        if (task.result?.error != null) {
          val error = task.result?.error
          listener?.get()?.onPasswordListFetchFailed(error?.code, error?.message, this)
        } else {
          passwords = task.result?.passwords
          listener?.get()?.onPasswordListFetched(task.result?.passwords, this)
        }
      }
    }
  }
}