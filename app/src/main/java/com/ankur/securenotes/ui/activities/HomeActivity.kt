package com.ankur.securenotes.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.databinding.ActivityHomeBinding
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.ui.fragments.note.list.NoteListFragment
import com.ankur.securenotes.ui.fragments.password.list.PasswordListFragment
import java.lang.ref.WeakReference

class HomeActivity : BaseActivity(), NoteListFragment.Listener, PasswordListFragment.Listener {

  private lateinit var binding: ActivityHomeBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityHomeBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    setSupportActionBar(binding.toolbar)

    showNoteListFragment()
    setFabActionListeners()
    setBottomNavigationActionListeners()
  }

  private fun setFabActionListeners() {
    binding.fabAddNoteButton.setOnClickListener {
      showOptionsDialog()
    }
  }

  private fun setBottomNavigationActionListeners() {
    binding.bottomNavigationView.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.navigation_notes -> {
          hidePasswordListFragment()
          showNoteListFragment()
          true
        }
        R.id.navigation_passwords -> {
          hideNoteListFragment()
          showPasswordListFragment()
          true
        }
        else -> {
          false
        }
      }
    }
  }

  private fun showNoteListFragment() {
    val fragment = showFragment(NoteListFragment.TAG, null)
    (fragment as? NoteListFragment)?.setListener(this)
  }

  private fun hideNoteListFragment() {
    removeFragment(NoteListFragment.TAG)
  }

  private fun showPasswordListFragment() {
    val fragment = showFragment(PasswordListFragment.TAG, null)
    (fragment as? PasswordListFragment)?.setListener(this)
  }

  private fun hidePasswordListFragment() {
    removeFragment(PasswordListFragment.TAG)
  }

  override fun newFragment(tag: String): Fragment? {
    when (tag) {
      NoteListFragment.TAG -> return NoteListFragment()
      PasswordListFragment.TAG -> return PasswordListFragment()
    }

    return super.newFragment(tag)
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
    val items = arrayOf("Create a note", "Create a password")

    AlertDialog.Builder(this).setItems(items) { _, which ->
      if ("Create a note" == items[which]) {
        openNoteEditor()
      } else if ("Create a password" == items[which]) {
        val intent = Intent(this, PasswordEditorActivity::class.java)
        intent.putExtra(
          PasswordEditorActivity.PARAM_MODE_FLAG, PasswordEditorActivity.MODE_CREATE
        )
        startActivity(intent)
      }
    }.show()
  }

  override fun onPasswordItemSelected(
    password: PasswordEntity, fragment: WeakReference<Fragment>
  ) {
    val intent = Intent(this, PasswordEditorActivity::class.java)
    intent.putExtra(PasswordEditorActivity.PARAM_MODE_FLAG, NoteEditorActivity.MODE_VIEW)
    intent.putExtra(PasswordEditorActivity.PARAM_PASSWORD_ID, password.id)
    startActivity(intent)
  }
}