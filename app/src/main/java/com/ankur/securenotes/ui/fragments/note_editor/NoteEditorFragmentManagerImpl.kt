package com.ankur.securenotes.ui.fragments.note_editor

import android.content.Context
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError
import com.ankur.securenotes.tasks.CreateNoteTask
import com.ankur.securenotes.tasks.UpdateNoteTask
import java.lang.ref.WeakReference

class NoteEditorFragmentManagerImpl: NoteEditorFragmentManager, SerialTaskExecutor.Listener {
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

    }

    override fun onTaskStarted(task: Task) {
        if (task is CreateNoteTask) {
            listener?.get()?.onNoteSavingStarted(task.note, WeakReference(this))
        } else if (task is UpdateNoteTask) {
            listener?.get()?.onNoteSavingStarted(task.note, WeakReference(this))
        }
    }

    override fun onTaskFinished(task: Task) {
        var error: TaskError? = null
        var note: NoteEntity? = null

        when(task) {
            is CreateNoteTask -> {
                error = task.result?.error
                note = task.result?.note
            }

            is UpdateNoteTask -> {
                error = task.result?.error
                note = task.result?.note
            }
        }

        error?.let {
            listener?.get()?.onNoteSavingFailed(note, WeakReference(this))

            return
        }

        listener?.get()?.onNoteSaved(note, WeakReference(this))
    }
}