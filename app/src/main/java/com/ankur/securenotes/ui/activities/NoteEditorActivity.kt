package com.ankur.securenotes.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.ui.fragments.note_editor.NoteEditorFragment
import kotlinx.android.synthetic.main.activity_note_editor.*
import java.lang.ref.WeakReference


class NoteEditorActivity : AppCompatActivity(), NoteEditorFragment.Listener, View.OnClickListener {

    // region Properties
    private var mode: String? = MODE_CREATE
    private var noteId: String? = null
    private var menuItemIdsValidForMode: Array<Int> = emptyArray()
    // endregion

    // region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        setSupportActionBar(findViewById(R.id.toolbar))

        setArgs()
        setupToolbar()
        showNoteEditorFragment()
        setupActionListeners()
    }
    // endregion

    // region Methods
    private fun setupToolbar() {
        when(mode) {
            MODE_VIEW -> {
                supportActionBar?.title = ""
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miEditButton, R.id.miDeleteButton)
            }

            MODE_CREATE -> {
                supportActionBar?.title = "New Note"
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }

            MODE_EDIT -> {
                supportActionBar?.title = "Edit Note"
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }
        }
    }

    private fun setArgs() {
        mode = intent.extras?.get(PARAM_MODE_FLAG) as? String
        noteId = intent.extras?.get(PARAM_NOTE_ID) as? String
    }

    private fun setupActionListeners() {
        toolbar.setNavigationOnClickListener {
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

    private fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun isFragmentPresent(tag: String): Boolean {
        return findFragment(tag) != null
    }

    private fun showNoteEditorFragment() {
        if (isFragmentPresent(NoteEditorFragment.TAG)) {
            return
        }

        // Add the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val noteEditorFragment = NoteEditorFragment()
        noteEditorFragment.setListener(this)

        // Set arguments
        val bundle = Bundle()
        if ((mode == MODE_EDIT || mode == MODE_VIEW) && noteId != null) {
            bundle.putString(NoteEditorFragment.PARAM_NOTE_ID, noteId)
        }
        if (mode == MODE_VIEW) {
            bundle.putString(NoteEditorFragment.PARAM_MODE_FLAG, NoteEditorFragment.MODE_VIEW)
        } else if (mode == MODE_EDIT) {
            bundle.putString(NoteEditorFragment.PARAM_MODE_FLAG, NoteEditorFragment.MODE_EDIT)
        }
        if (bundle.keySet().count() > 0) {
            noteEditorFragment.arguments = bundle
        }

        // Show
        fragmentTransaction.add(R.id.fragmentContainer, noteEditorFragment, NoteEditorFragment.TAG)
        fragmentTransaction.commit()
    }

    private fun hideNoteEditorFragment() {
        val noteEditorFragment = findFragment(NoteEditorFragment.TAG) ?: return

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(noteEditorFragment)
        fragmentTransaction.commit()
    }

    private fun saveNote() {
        val noteEditorFragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
        noteEditorFragment?.saveNote()
    }

    private fun editNote() {
        // Set Activity mode
        mode = MODE_EDIT

        // Set fragment mode
        val fragment= findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
        fragment?.setMode(NoteEditorFragment.MODE_EDIT)

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun deleteNote() {
        Toast.makeText(this, "Deleting Note, Please wait.", Toast.LENGTH_SHORT).show()
    }

    private fun showDiscardChangesConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Discard Changes")
            .setMessage("Are you sure you want to discard your changes?")
            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                discardChangesAndSwitchToViewMode()
            })
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun showCancelCreateConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Discard Changes")
            .setMessage("Are you sure you want to discard your changes?")
            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                cancelNoteCreate()
            })
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun discardChangesAndSwitchToViewMode() {
        // Set Activity mode
        mode = MODE_VIEW

        // Set fragment mode
        val fragment= findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
        fragment?.discardChanges()
        fragment?.setMode(NoteEditorFragment.MODE_VIEW)

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun cancelNoteCreate() {
        finish()
    }

    // endregion

    // region Overridden Methods
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
                deleteNote()

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
    // endregion

    // region NoteEditorFragment.Listener
    override fun onNoteSaved(note: NoteEntity, fragment: WeakReference<Fragment>) {
        Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show()

        finish()
    }

    override fun onNoteSavingFailed(note: NoteEntity?, message: String?, fragment: WeakReference<Fragment>) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            return
        }

        Toast.makeText(this, "Could not save note.", Toast.LENGTH_SHORT).show()
    }
    // endregion

    override fun onClick(v: View?) {

    }
    // endregion

    companion object {
        const val PARAM_NOTE_ID = "PARAM_NOTE_ID"
        const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"

        const val MODE_CREATE = "MODE_CREATE"
        const val MODE_EDIT = "MODE_EDIT"
        const val MODE_VIEW = "MODE_VIEW"
    }
}