package com.ankur.securenotes.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.databinding.ActivityPasswordEditorBinding
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.ui.fragments.password.editor.PasswordEditorFragment
import com.ankur.securenotes.ui.fragments.password.viewer.PasswordViewerFragment
import java.lang.ref.WeakReference

class PasswordEditorActivity : BaseActivity(), PasswordEditorFragment.Listener {

  private lateinit var binding: ActivityPasswordEditorBinding

  private var mode: String? = MODE_CREATE
  private var passwordId: String? = null
  private var menuItemIdsValidForMode: Array<Int> = emptyArray()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityPasswordEditorBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    setSupportActionBar(binding.toolbar)

    setArgs()
    setupToolbar()
    showStartingFragment()
    setupActionListeners()
  }

  private fun setArgs() {
    mode = intent.extras?.get(PARAM_MODE_FLAG) as? String
    passwordId = intent.extras?.get(PARAM_PASSWORD_ID) as? String
  }

  private fun setupToolbar() {
    when (mode) {
      MODE_VIEW -> {
        supportActionBar?.title = getString(R.string.password_editor_title_view_password)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miEditButton, R.id.miDeleteButton)
      }

      MODE_CREATE -> {
        supportActionBar?.title = getString(R.string.password_editor_title_new_password)
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
      }

      MODE_EDIT -> {
        supportActionBar?.title = getString(R.string.password_editor_title_edit_password)
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
        menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
      }
    }
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

  private fun showStartingFragment() {
    if (isInViewMode()) {
      showPasswordViewerFragment()
    } else if (isInEditOrCreateMode()) {
      showPasswordEditorFragment()
    }
  }

  private fun showPasswordViewerFragment() {
    showFragment(PasswordViewerFragment.TAG, getBundleForChildFragment())
  }

  private fun showPasswordEditorFragment() {
    val fragment = showFragment(PasswordEditorFragment.TAG, getBundleForChildFragment())
    (fragment as? PasswordEditorFragment)?.setListener(this)
  }

  private fun getBundleForChildFragment(): Bundle {
    val bundle = Bundle()
    if (hasPasswordToViewOrEdit()) {
      bundle.putString(PasswordEditorFragment.PARAM_PASSWORD_ID, passwordId)
    }

    return bundle
  }

  private fun hasPasswordToViewOrEdit(): Boolean {
    return ((mode == MODE_EDIT || mode == MODE_VIEW) && passwordId != null)
  }

  private fun isInViewMode(): Boolean {
    return (mode == MODE_VIEW)
  }

  private fun isInEditOrCreateMode(): Boolean {
    return (mode == MODE_EDIT || mode == MODE_CREATE)
  }

  private fun savePassword() {
    val passwordEditorFragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
    passwordEditorFragment?.savePassword()
  }

  private fun editPassword() { // Set Activity mode
    mode = MODE_EDIT

    showPasswordEditorFragment()

    // Refresh the toolbar
    setupToolbar()
    invalidateOptionsMenu()
  }

  private fun deletePassword() { // Set fragment mode
    val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
    fragment?.deletePassword()
  }

  private fun showDeletePasswordConfirmationDialog() {
    AlertDialog.Builder(this).setTitle(getString(R.string.password_editor_delete_confirmation_dialog_title))
      .setMessage(getString(R.string.password_editor_delete_confirmation_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        deletePassword()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun showDiscardChangesConfirmationDialog() {
    val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
    if (fragment?.hasEditableChanges() == false) {
      discardChangesAndSwitchToViewMode()

      return
    }

    AlertDialog.Builder(this).setTitle(getString(R.string.password_editor_discard_changes_dialog_title))
      .setMessage(getString(R.string.password_editor_discard_changes_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        discardChangesAndSwitchToViewMode()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun showCancelCreateConfirmationDialog() {
    val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
    if (fragment?.hasEditableChanges() == false) {
      cancelPasswordCreate()

      return
    }

    AlertDialog.Builder(this).setTitle(getString(R.string.password_editor_cancel_create_dialog_title))
      .setMessage(getString(R.string.password_editor_cancel_create_dialog_body))
      .setPositiveButton(R.string.common_dialog_option_yes) { _, _ ->
        cancelPasswordCreate()
      }.setNegativeButton(R.string.common_dialog_option_no, null).show()
  }

  private fun discardChangesAndSwitchToViewMode() {
    mode = MODE_VIEW
    showPasswordViewerFragment()

    // Refresh the toolbar
    setupToolbar()
    invalidateOptionsMenu()
  }

  private fun cancelPasswordCreate() {
    finish()
  }

  override fun newFragment(tag: String): Fragment? {
    when (tag) {
      PasswordViewerFragment.TAG -> return PasswordViewerFragment()
      PasswordEditorFragment.TAG -> return PasswordEditorFragment()
    }

    return null
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_password_editor, menu)

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.miSaveButton -> {
        savePassword()

        true
      }

      R.id.miDeleteButton -> {
        showDeletePasswordConfirmationDialog()

        true
      }

      R.id.miEditButton -> {
        editPassword()

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

    menuItemsToRemove.forEach { menu?.removeItem(it) }

    return super.onPrepareOptionsMenu(menu)
  }

  override fun onPasswordSaved(
    password: PasswordEntity, fragment: WeakReference<Fragment>
  ) {
    Toast.makeText(
      this, getString(R.string.password_editor_message_password_saved), Toast.LENGTH_SHORT
    ).show()
    finish()
  }

  override fun onPasswordSavingFailed(password: PasswordEntity?, message: String?, fragment: WeakReference<Fragment>) {
    if (message != null) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
  }

  override fun onPasswordDeleted(password: PasswordEntity, fragment: WeakReference<Fragment>) {
    Toast.makeText(
      this, getString(R.string.password_editor_message_password_delete), Toast.LENGTH_SHORT
    ).show()
    finish()
  }

  override fun onPasswordDeletionFailed(password: PasswordEntity, message: String?, fragment: WeakReference<Fragment>) {
    if (message != null) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
  }

  companion object {
    const val PARAM_PASSWORD_ID = "PARAM_PASSWORD_ID"
    const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"

    const val MODE_CREATE = "MODE_CREATE"
    const val MODE_EDIT = "MODE_EDIT"
    const val MODE_VIEW = "MODE_VIEW"
  }
}