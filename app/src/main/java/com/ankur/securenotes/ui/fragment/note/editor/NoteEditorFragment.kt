package com.ankur.securenotes.ui.fragment.note.editor

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ankur.securenotes.databinding.FragmentNoteEditorBinding
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.task.GetNoteByIdTask
import java.lang.ref.WeakReference

class NoteEditorFragment : Fragment(), NoteEditorFragmentManager.Listener, SerialTaskExecutor.Listener {

  interface Listener {
    fun onNoteSaved(note: NoteEntity, fragment: WeakReference<Fragment>)
    fun onNoteSavingFailed(note: NoteEntity?, message: String?, fragment: WeakReference<Fragment>)
    fun onNoteDeleted(note: NoteEntity, fragment: WeakReference<Fragment>)
    fun onNoteDeletionFailed(note: NoteEntity, message: String?, fragment: WeakReference<Fragment>)
  }

  private lateinit var binding: FragmentNoteEditorBinding
  private lateinit var activity: Activity
  private lateinit var manager: NoteEditorFragmentManager

  private var mode: String? = MODE_EDIT
  private var noteId: String? = null
  private var listener: WeakReference<Listener>? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)

    activity = context as Activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setArgs()
    setDependencies(savedInstanceState)

    fetchNote()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = FragmentNoteEditorBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onResume() {
    super.onResume()

    reloadData()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    if (activity.isChangingConfigurations) {
      outState.putBoolean("isChangingConfiguration", true)
      Shared.store!!.save(TAG, "manager", manager)
    } else {
      outState.putBoolean("isChangingConfiguration", false)
    }
  }

  fun setListener(listener: Listener) {
    this.listener = WeakReference(listener)
  }

  fun setMode(mode: String) {
    when (mode) {
      MODE_EDIT, MODE_VIEW -> {
        this.mode = mode
      }
    }

    updateUiState()
  }

  fun saveNote() {
    val title = binding.etTitleEditText.text.toString()
    val body = binding.etBodyEditText.text.toString()

    if (body.isEmpty()) {
      listener?.get()?.onNoteSavingFailed(null, "Error: Cannot save an empty note.", WeakReference(this))

      return
    }

    if (manager.note == null) {
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

  fun deleteNote() {
    manager.deleteNote()
  }

  fun discardChanges() {
    reloadData()
  }

  fun hasEditableChanges(): Boolean {
    val title = binding.etTitleEditText.text.toString()
    val body = binding.etBodyEditText.text.toString()

    return if (noteId == null) {
      !(title.isEmpty() && body.isEmpty())
    } else {
      !(title == manager.note?.title && body == manager.note?.body)
    }
  }

  private fun setArgs() {
    noteId = arguments?.get(PARAM_NOTE_ID) as? String
    mode = arguments?.get(PARAM_MODE_FLAG) as? String
  }

  private fun setDependencies(savedInstanceState: Bundle?) {
    manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
      Shared.store?.retrieve(TAG, "manager") as NoteEditorFragmentManager
    } else {
      context?.let {
        NoteEditorFragmentManagerBuilder().set(context = activity).set(listener = this).build()
      }!!
    }
  }

  private fun fetchNote() {
    this.noteId?.let {
      val getNoteTask = GetNoteByIdTask(it, Shared.getReadableDatabase(activity))
      Shared.serialTaskExecutor?.exec(getNoteTask, this)
    }
  }

  private fun updateUiState() {
    if (mode == MODE_VIEW) {
      binding.etTitleEditText.isEnabled = false
      binding.etBodyEditText.isEnabled = false
    } else {
      binding.etTitleEditText.isEnabled = true
      binding.etBodyEditText.isEnabled = true
    }
  }

  private fun reloadData() {
    val note = manager.note
    binding.etTitleEditText.setText(note?.title, TextView.BufferType.EDITABLE)
    binding.etBodyEditText.setText(note?.body, TextView.BufferType.EDITABLE)
  }

  override fun onTaskStarted(task: Task) {

  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is GetNoteByIdTask -> {
        task.result?.note?.let {
          manager.note = it

          updateUiState()
          reloadData()
        }
      }
    }
  }

  override fun onNoteSavingStarted(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {

  }

  override fun onNoteSaved(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {
    listener?.get()?.onNoteSaved(note, WeakReference(this))
  }

  override fun onNoteSavingFailed(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {
    listener?.get()?.onNoteSavingFailed(note, "Error: Could not save note.", WeakReference(this))
  }

  override fun onNoteDeletionStarted(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {

  }

  override fun onNoteDeleted(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {
    listener?.get()?.onNoteDeleted(note, WeakReference(this))
  }

  override fun onNoteDeletionFailed(note: NoteEntity, manager: WeakReference<NoteEditorFragmentManager>?) {
    listener?.get()?.onNoteDeletionFailed(note, "Error: Could not delete note.", WeakReference(this))
  }

  companion object {

    @JvmField
    val TAG: String = this::class.java.name

    const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"
    const val PARAM_NOTE_ID = "PARAM_NOTE_ID"

    const val MODE_EDIT = "MODE_EDIT"
    const val MODE_VIEW = "MODE_VIEW"
  }
}