package com.ankur.securenotes.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.ui.fragments.note_list.NoteListFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.ref.WeakReference

class HomeActivity : AppCompatActivity(), NoteListFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))

        showNoteListFragment()

        fabAddNoteButton.setOnClickListener {
            openNoteEditor()
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
        fragmentTransaction.add(R.id.fragmentContainer, noteListFragment,
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
        intent.putExtra(NoteEditorActivity.EDITOR_MODE_FLAG, NoteEditorActivity.EDITOR_MODE_CREATE)
        startActivity(intent)
    }

    override fun onNoteItemSelected(note: NoteEntity, fragment: WeakReference<Fragment>) {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra(NoteEditorActivity.EDITOR_MODE_FLAG, NoteEditorActivity.EDITOR_MODE_EDIT)
        intent.putExtra(NoteEditorActivity.EDITOR_NOTE_ID, note.id )
        startActivity(intent)
    }
}