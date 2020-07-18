package com.ankur.securenotes.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.ui.fragments.note_editor.NoteEditorFragment
import java.lang.ref.WeakReference


class NoteEditorActivity : AppCompatActivity(), NoteEditorFragment.Listener {

    private var mode: String? = EDITOR_MODE_CREATE
    private var noteUuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        setSupportActionBar(findViewById(R.id.toolbar))

        mode = intent.extras?.get(EDITOR_MODE_FLAG) as? String
        noteUuid = intent.extras?.get(EDITOR_NOTE_UUID) as? String

        setupToolbar()
        showNoteEditorFragment()
    }

    private fun setupToolbar() {
        val title = when(intent.extras?.get(EDITOR_MODE_FLAG)) {
            EDITOR_MODE_CREATE -> "Create Note"
            EDITOR_MODE_EDIT -> "Edit Note"
            else -> ""
        }

        supportActionBar?.title = title
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
        if (mode == EDITOR_MODE_EDIT && noteUuid != null) {
            val bundle = Bundle()
            bundle.putString("noteUuid", noteUuid)

            noteEditorFragment.arguments = bundle
        }

        noteEditorFragment.setListener(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_note_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miSaveButton -> {
                val noteEditorFragment = findFragment(NoteEditorFragment.TAG) as? NoteEditorFragment
                noteEditorFragment?.saveNote()

                true
            }
            R.id.miDeleteButton -> {
                Toast.makeText(this, "Deleting Note, Please wait.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val mode = intent.extras?.get(EDITOR_MODE_FLAG)
        if (mode == EDITOR_MODE_CREATE) {
            menu?.removeItem(R.id.miDeleteButton)
        }

        return super.onPrepareOptionsMenu(menu)
    }

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

    companion object {
        const val EDITOR_NOTE_UUID = "EDITOR_NOTE_UUID"
        const val EDITOR_MODE_FLAG = "EDITOR_MODE_FLAG"
        const val EDITOR_MODE_CREATE = "CREATE"
        const val EDITOR_MODE_EDIT = "EDIT"
    }
}