package com.ankur.securenotes.ui.fragments.password.editor

import android.content.Context
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.tasks.CreatePasswordTask
import com.ankur.securenotes.tasks.DeletePasswordTask
import com.ankur.securenotes.tasks.UpdatePasswordTask
import java.lang.ref.WeakReference

class PasswordEditorFragmentManagerImpl :
    PasswordEditorFragmentManager,
    SerialTaskExecutor.Listener {

    override var password: PasswordEntity? = null
    override var context: Context? = null
    override var listener: WeakReference<PasswordEditorFragmentManager.Listener>? = null

    override fun savePassword() {
        if (password == null || context == null) {
            return
        }

        if (password?.id == null) {
            val createTask = CreatePasswordTask(password!!, Shared.getWritableDatabase(context!!))
            Shared.serialTaskExecutor?.exec(createTask, this)
        } else {
            val updateTask = UpdatePasswordTask(password!!, Shared.getWritableDatabase(context!!))
            Shared.serialTaskExecutor?.exec(updateTask, this)
        }
    }

    override fun deletePassword() {
        if (password == null || password?.id == null || context == null) {
            return
        }

        val deleteTask = DeletePasswordTask(password!!, Shared.getWritableDatabase(context!!))
        Shared.serialTaskExecutor?.exec(deleteTask, this)
    }

    override fun onTaskStarted(
        task: Task
    ) {
        when (task) {
            is CreatePasswordTask -> {
                listener?.get()
                    ?.onPasswordSavingStarted(task.password, WeakReference(this))
            }
            is UpdatePasswordTask -> {
                listener?.get()
                    ?.onPasswordSavingStarted(task.password, WeakReference(this))
            }
            is DeletePasswordTask -> {
                listener?.get()
                    ?.onPasswordDeletionStarted(task.password, WeakReference(this))
            }
        }
    }

    override fun onTaskFinished(
        task: Task
    ) {
        when (task) {
            is CreatePasswordTask -> {
                task.result?.error?.let {
                    listener?.get()
                        ?.onPasswordSavingFailed(task.password, WeakReference(this))

                    return
                }

                task.result?.password?.let {
                    listener?.get()
                        ?.onPasswordSaved(it, WeakReference(this))
                }
            }

            is UpdatePasswordTask -> {
                task.result?.error?.let {
                    listener?.get()
                        ?.onPasswordSavingFailed(task.password, WeakReference(this))

                    return
                }

                task.result?.password?.let {
                    listener?.get()
                        ?.onPasswordSaved(it, WeakReference(this))
                }
            }

            is DeletePasswordTask -> {
                task.result?.error?.let {
                    listener?.get()
                        ?.onPasswordDeletionFailed(task.password, WeakReference(this))

                    return
                }

                listener?.get()
                    ?.onPasswordDeleted(task.password, WeakReference(this))
            }
        }
    }
}