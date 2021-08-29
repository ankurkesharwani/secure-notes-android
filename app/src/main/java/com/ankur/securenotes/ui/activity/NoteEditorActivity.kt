package com.ankur.securenotes.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.databinding.ActivityNoteEditorBinding
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.ui.fragment.note.editor.NoteEditorFragment
import java.lang.ref.WeakReference

class NoteEditorActivity : BaseActivity(), NoteEditorFragment.Listener {

  private lateinit var binding: ActivityNoteEditorBinding

  private var mode: String? = MODE_CREATE
  private var noteId: String? = null
  private var menuItemIdsValidForMode: Array<Int> = emptyArray()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityNoteEditorBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    setSupportActionBar(binding.toolbar)

    setArgs()
    setupToolbar()
    showNoteEditorFragment()
    setupActionListeners()
  }

  private fun setupToolbar() {
    when (mode) {
      MODE_VIEW -> {
        supportActionBar?.title = getString(R.string.note_editor_title_view_note)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miEditButton, R.id.miDeleteButton)
      }

      MODE_CREATE -> {
        supportActionBar?.title = getString(R.string.note_editor_title_new_note)
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
      }

      MODE_EDIT -> {
        supportActionBar?.title = getString(R.string.note_editor_title_edit_note)
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
      }
    }
  }

  private fun setArgs() {
    mode = intent.extras?.get(PARAM_MODE_FLAG) as? String
    noteId = intent.extras?.get(PARAM_NOTE_ID) as? String
  }

  private fun setupActionListeners() {
    binding.toolbar.setNavigationOnClickListener {
      when (mode) {
        MODE_VIEW -> {
          finish()
        }

        MODE_CREATE -> {
          showCancelCreateConfirmationDialog()
        }

        MODE_EDIT -> {
          showDiscardChangesConfirmationDialog()
        }
      }
    }
  }

  private fun showNoteEditorFragment() {
    val fragment = showFragment(NoteEditorFragment.TAG, getBundleForChildFragment())
    (fragment as NoteEditorFragment).setListener(this)
  }

  private fun hideNoteEditorFragment() {
    removeFragment(NoteEditorFragment.TAG)
  }

  override fun newFragment(tag: String): Fragment? {
    when (tag) {
      NoteEditorFragment.TAG -> return NoteEditorFragment()
    }

    return super.newFragment(tag)
  }

  private fun getBundleForChildFragment(): Bundle {
    val bundle = Bundle()
    if (hasNoteToView()) {
      bundle.putString(NoteEditorFragment.PARAM_NOTE_ID, noteId)
    }
    if (isInViewMode()) {
      bundle.putString(NoteEditorFragment.PARAM_MODE_FLAG, NoteEditorFragment.MODE_VIEW)
    } else if (isInEditMode()) {
      bundle.putString(NoteEditorFragment.PARAM_MODE_FLAG, NoteEditorFragment.MODE_EDIT)
    }

    return bundle
  }

  private fun hasNoteToView(): Boolean {
    return ((mode == MODE_EDIT || mode == MODE_VIEW) && noteId != null)
  }

  private fun isInViewMode(): Boolean {
    return (mode == MODE_VIEW)
  }

  private fun isInEditMode(): Boolean {
    return (mode == MODE_VIEW)
  }

  private fun saveNote() {
    val noteEditorFragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    noteEditorFragment?.saveNote()
  }

  private fun editNote() { // Set Activity mode
    mode = MODE_EDIT

    // Set fragment mode
    val fragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    fragment?.setMode(NoteEditorFragment.MODE_EDIT)

    // Refresh the toolbar
    setupToolbar()
    invalidateOptionsMenu()
  }

  private fun showDeleteNoteConfirmationDialog() {
    AlertDialog.Builder(this).setTitle(getString(R.string.note_editor_delete_confirmation_dialog_title))
      .setMessage(getString(R.string.note_editor_delete_confirmation_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        deleteNote()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun showDiscardChangesConfirmationDialog() {
    val fragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    if (fragment?.hasEditableChanges() == false) {
      discardChangesAndSwitchToViewMode()

      return
    }

    AlertDialog.Builder(this).setTitle(getString(R.string.note_editor_discard_changes_dialog_title))
      .setMessage(getString(R.string.note_editor_discard_changes_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        discardChangesAndSwitchToViewMode()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun showCancelCreateConfirmationDialog() {
    val fragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    if (fragment?.hasEditableChanges() == false) {
      cancelNoteCreate()

      return
    }

    AlertDialog.Builder(this).setTitle(getString(R.string.note_editor_cancel_create_dialog_title))
      .setMessage(getString(R.string.note_editor_cancel_create_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        cancelNoteCreate()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun discardChangesAndSwitchToViewMode() { // Set Activity mode
    mode = MODE_VIEW

    // Set fragment mode
    val fragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    fragment?.discardChanges()
    fragment?.setMode(NoteEditorFragment.MODE_VIEW)

    // Refresh the toolbar
    setupToolbar()
    invalidateOptionsMenu()
  }

  private fun cancelNoteCreate() {
    finish()
  }

  private fun deleteNote() { // Set fragment mode
    val fragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
    fragment?.deleteNote()
  }


  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_note_editor, menu)

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.miSaveButton -> {
        saveNote()

        true
      }

      R.id.miDeleteButton -> {
        showDeleteNoteConfirmationDialog()

        true
      }

      R.id.miEditButton -> {
        editNote()

        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    val iterator = menu?.iterator()
    val menuItemsToRemove: MutableList<Int> = mutableListOf()
    while (iterator?.hasNext() == true) {
      val menuItem = iterator.next()
      if (!menuItemIdsValidForMode.contains(menuItem.itemId)) {
        menuItemsToRemove.add(menuItem.itemId)
      }
    }

    menuItemsToRemove.forEach {
      menu?.removeItem(it)
    }

    return super.onPrepareOptionsMenu(menu)
  }

  override fun onNoteSaved(note: NoteEntity, fragment: WeakReference<Fragment>) {
    Toast.makeText(this, getString(R.string.note_editor_message_note_saved), Toast.LENGTH_SHORT).show()

    finish()
  }

  override fun onNoteSavingFailed(note: NoteEntity?, message: String?, fragment: WeakReference<Fragment>) {
    if (message != null) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
  }

  override fun onNoteDeleted(note: NoteEntity, fragment: WeakReference<Fragment>) {
    Toast.makeText(
      this, getString(R.string.note_editor_message_note_delete), Toast.LENGTH_SHORT
    ).show()

    finish()
  }

  override fun onNoteDeletionFailed(note: NoteEntity, message: String?, fragment: WeakReference<Fragment>) {
    if (message != null) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
  }

  companion object {
    const val PARAM_NOTE_ID = "PARAM_NOTE_ID"
    const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"

    const val MODE_CREATE = "MODE_CREATE"
    const val MODE_EDIT = "MODE_EDIT"
    const val MODE_VIEW = "MODE_VIEW"
  }
}