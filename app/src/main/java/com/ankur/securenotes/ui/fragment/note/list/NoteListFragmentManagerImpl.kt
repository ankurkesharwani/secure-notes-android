package com.ankur.securenotes.ui.fragment.note.list

import android.content.Context
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.task.GetAllNotesTask
import java.lang.ref.WeakReference

class NoteListFragmentManagerImpl : NoteListFragmentManager, SerialTaskExecutor.Listener {

  override var notes: List<NoteEntity>? = null
  override var context: Context? = null
  override var listener: WeakReference<NoteListFragmentManager.Listener>? = null

  override fun fetchNoteList() {
    context?.let {
      Shared.serialTaskExecutor?.exec(GetAllNotesTask(Shared.getReadableDatabase(it)), this)
    }
  }

  override fun onTaskStarted(task: Task) {
    if (task is GetAllNotesTask) {
      listener?.get()?.onNoteListFetchStart(this)
    }
  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is GetAllNotesTask -> {
        if (task.result?.error != null) {
          val error = task.result?.error
          listener?.get()?.onNoteListFetchFailed(error?.code, error?.message, this)
        } else {
          notes = task.result?.notes
          listener?.get()?.onNoteListFetched(task.result?.notes, this)
        }
      }
    }
  }
}