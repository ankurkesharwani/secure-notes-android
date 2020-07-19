package com.ankur.securenotes.ui.fragments.note_editor

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.tasks.GetNoteByIdTask
import kotlinx.android.synthetic.main.fragment_note_editor.*
import java.lang.ref.WeakReference


class NoteEditorFragment : Fragment(),
    NoteEditorFragmentManager.Listener,
    SerialTaskExecutor.Listener {

    // region Declarations
    interface Listener {
        fun onNoteSaved(note: NoteEntity, fragment: WeakReference<Fragment>)
        fun onNoteSavingFailed(note: NoteEntity?, message: String?, fragment: WeakReference<Fragment>)
    }
    // endregion

    // region Properties
    private lateinit var activity: Activity
    private lateinit var manager: NoteEditorFragmentManager
    private var listener: WeakReference<Listener>? = null
    // endregion
    
    // region Lifecycle
    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
            Shared.store?.retrieve(TAG, "manager") as NoteEditorFragmentManager
        } else {
            context?.let { NoteEditorFragmentManagerBuilder()
                .set(context = activity)
                .set(listener = this).build() }!!
        }

        arguments?.get(PARAM_NOTE_ID)?.let {
            fetchNoteToEdit(it as String)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_editor, container, false)
    }

    /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        fetchData()
    }

    override fun onResume() {
        super.onResume()

        reloadData()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
    */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (activity.isChangingConfigurations) {
            outState.putBoolean("isChangingConfiguration", true)
            Shared.store!!.save(TAG, "manager", manager)
        } else {
            outState.putBoolean("isChangingConfiguration", false)
        }
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
    */
    // endregion


    // region Methods
    fun setListener(listener: Listener) {
        this.listener = WeakReference(listener)
    }

    private fun fetchNoteToEdit(noteId: String) {
        val getNoteTask = GetNoteByIdTask(noteId, Shared.getReadableDatabase(activity))
        Shared.serialTaskExecutor?.exec(getNoteTask, this)
    }

    private fun reloadData() {
        val note = manager.note
        etTitleEditText.setText(note?.title, TextView.BufferType.EDITABLE)
        etBodyEditText.setText(note?.body, TextView.BufferType.EDITABLE)
    }

    fun saveNote() {
        val title = etTitleEditText.text.toString()
        val body = etBodyEditText.text.toString()

        if (body.isEmpty()) {
            listener?.get()?.onNoteSavingFailed(null, "Cannot save an empty note.", WeakReference(this))

            return
        }

        if(manager.note == null) {
            val note = NoteEntity()
            note.title = title
            note.body = body
            note.archived = false

            manager.note = note
        } else {
            manager.note?.title = title
            manager.note?.body = body
        }

        manager.saveNote()
    }
    // endregion

    // region SerialTaskExecutor.Listener
    override fun onTaskStarted(task: Task) {

    }

    override fun onTaskFinished(task: Task) {
        when(task) {
            is GetNoteByIdTask -> {
                task.result?.note?.let {
                    manager.note = it

                    reloadData()
                }
            }
        }
    }
    // endregion

    // region NoteEditorFragmentManager.Listener
    override fun onNoteSavingStarted(note: NoteEntity?, manager: WeakReference<NoteEditorFragmentManager>?) {

    }

    override fun onNoteSaved(note: NoteEntity?, manager: WeakReference<NoteEditorFragmentManager>?) {
        note?.let {
            listener?.get()?.onNoteSaved(note, WeakReference(this))
        }
    }

    override fun onNoteSavingFailed(note: NoteEntity?, manager: WeakReference<NoteEditorFragmentManager>?) {
        note?.let {
            listener?.get()?.onNoteSavingFailed(note, null, WeakReference(this))
        }
    }
    // endregion

    companion object {

        @JvmField
        val TAG = this::class.java.name

        const val PARAM_NOTE_ID = "noteId"
    }
}