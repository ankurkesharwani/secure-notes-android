package com.ankur.securenotes.ui.fragment.note.editor

import android.content.Context
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.task.CreateNoteTask
import com.ankur.securenotes.task.DeleteNoteTask
import com.ankur.securenotes.task.UpdateNoteTask
import java.lang.ref.WeakReference

class NoteEditorFragmentManagerImpl : NoteEditorFragmentManager, SerialTaskExecutor.Listener {

  override var note: NoteEntity? = null
  override var context: Context? = null
  override var listener: WeakReference<NoteEditorFragmentManager.Listener>? = null

  override fun saveNote() {
    if (note == null || context == null) {
      return
    }

    if (note?.id == null) {
      val createTask = CreateNoteTask(note!!, Shared.getWritableDatabase(context!!))
      Shared.serialTaskExecutor?.exec(createTask, this)
    } else {
      val updateTask = UpdateNoteTask(note!!, Shared.getWritableDatabase(context!!))
      Shared.serialTaskExecutor?.exec(updateTask, this)
    }
  }

  override fun deleteNote() {
    if (note == null || note?.id == null || context == null) {
      return
    }

    val deleteTask = DeleteNoteTask(note!!, Shared.getWritableDatabase(context!!))
    Shared.serialTaskExecutor?.exec(deleteTask, this)
  }

  override fun onTaskStarted(task: Task) {
    when (task) {
      is CreateNoteTask -> {
        listener?.get()?.onNoteSavingStarted(task.note, WeakReference(this))
      }
      is UpdateNoteTask -> {
        listener?.get()?.onNoteSavingStarted(task.note, WeakReference(this))
      }
      is DeleteNoteTask -> {
        listener?.get()?.onNoteDeletionStarted(task.note, WeakReference(this))
      }
    }
  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is CreateNoteTask -> {
        task.result?.error?.let {
          listener?.get()?.onNoteSavingFailed(task.note, WeakReference(this))

          return
        }

        task.result?.note?.let {
          listener?.get()?.onNoteSaved(it, WeakReference(this))
        }
      }

      is UpdateNoteTask -> {
        task.result?.error?.let {
          listener?.get()?.onNoteSavingFailed(task.note, WeakReference(this))

          return
        }

        task.result?.note?.let {
          listener?.get()?.onNoteSaved(it, WeakReference(this))
        }
      }

      is DeleteNoteTask -> {
        task.result?.error?.let {
          listener?.get()?.onNoteDeletionFailed(task.note, WeakReference(this))

          return
        }

        listener?.get()?.onNoteDeleted(task.note, WeakReference(this))
      }
    }
  }
}