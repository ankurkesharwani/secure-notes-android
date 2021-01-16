package com.ankur.securenotes.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.ui.fragments.note_list.NoteListFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.fabAddNoteButton
import kotlinx.android.synthetic.main.activity_home_alternate.*
import java.lang.ref.WeakReference


class HomeActivity : AppCompatActivity(), NoteListFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_alternate)
        setSupportActionBar(findViewById(R.id.toolbar))

        showNoteListFragment()

        fabAddNoteButton.setOnClickListener {
            showOptionsDialog()
        }
    }

    private fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun isFragmentPresent(tag: String): Boolean {
        return findFragment(tag) != null
    }

    private fun showNoteListFragment() {
        if (isFragmentPresent(NoteListFragment.TAG)) {
            return
        }

        // Add the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val noteListFragment = NoteListFragment()
        noteListFragment.setListener(this)
        fragmentTransaction.add(
            R.id.fragmentContainer, noteListFragment,
            NoteListFragment.TAG
        )
        fragmentTransaction.commit()
    }

    private fun hideNoteListFragment() {
        val noteListFragment = findFragment(NoteListFragment.TAG) ?: return

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(noteListFragment)
        fragmentTransaction.commit()
    }

    private fun openNoteEditor() {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra(NoteEditorActivity.PARAM_MODE_FLAG, NoteEditorActivity.MODE_CREATE)
        startActivity(intent)
    }

    override fun onNoteItemSelected(note: NoteEntity, fragment: WeakReference<Fragment>) {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra(NoteEditorActivity.PARAM_MODE_FLAG, NoteEditorActivity.MODE_VIEW)
        intent.putExtra(NoteEditorActivity.PARAM_NOTE_ID, note.id)
        startActivity(intent)
    }

    private fun showOptionsDialog() {
        val items = arrayOf(
            "Create a note", "Create a password"
        )

        AlertDialog.Builder(this)
            .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                if ("Create a note" == items[which]) {
                    openNoteEditor()
                } else if ("Create a password" == items[which]) {
                    Snackbar.make(coordinatorLayout, "This is a sample snack bar", Snackbar.LENGTH_SHORT).show()
                }
            }).show()
    }
}