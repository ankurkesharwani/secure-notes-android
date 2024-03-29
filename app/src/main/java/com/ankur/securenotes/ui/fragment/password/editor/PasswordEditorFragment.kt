package com.ankur.securenotes.ui.fragment.password.editor

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.databinding.FragmentPasswordEditorBinding
import com.ankur.securenotes.entity.PasswordEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.task.GetPasswordByIdTask
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import java.lang.ref.WeakReference

class PasswordEditorFragment : Fragment(), PasswordEditorFragmentManager.Listener, SerialTaskExecutor.Listener,
                               View.OnClickListener {

  interface Listener {
    fun onPasswordSaved(password: PasswordEntity, fragment: WeakReference<Fragment>)
    fun onPasswordSavingFailed(password: PasswordEntity?, message: String?, fragment: WeakReference<Fragment>)
    fun onPasswordDeleted(password: PasswordEntity, fragment: WeakReference<Fragment>)
    fun onPasswordDeletionFailed(password: PasswordEntity, message: String?, fragment: WeakReference<Fragment>)
  }

  private lateinit var binding: FragmentPasswordEditorBinding
  private lateinit var activity: Activity
  private lateinit var manager: PasswordEditorFragmentManager

  private var passwordId: String? = null
  private var listener: WeakReference<Listener>? = null
  private var revealPassword = false

  override fun onAttach(context: Context) {
    super.onAttach(context)

    activity = context as Activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setArgs()
    setDependencies(savedInstanceState)
    fetchPassword()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = FragmentPasswordEditorBinding.inflate(layoutInflater, container, false)
    val view = binding.root

    setActionListeners()

    return view
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

  private fun setActionListeners() {
    binding.copyButton.setOnClickListener(this)
    binding.revealPasswordButton.setOnClickListener(this)
  }

  fun savePassword() {
    val title = binding.titleEditText.text.toString()
    val url = binding.urlEditText.text.toString()
    val email = binding.emailEditText.text.toString()
    val phone = binding.phoneEditText.text.toString()
    val username = binding.usernameEditText.text.toString()
    val passwordValue = binding.passwordEditText.text.toString()

    if (title.isEmpty()) {
      listener?.get()?.onPasswordSavingFailed(
        null, "Error: Cannot save an password without a title.", WeakReference(this)
      )

      return
    }

    if (url.isEmpty() && email.isEmpty() && phone.isEmpty() && username.isEmpty()) {
      listener?.get()?.onPasswordSavingFailed(
        null, "Error: Password must have an url, email, phone or username to save.", WeakReference(this)
      )

      return
    }

    if (manager.password == null) {
      val password = PasswordEntity()
      password.title = title
      password.url = url
      password.email = email
      password.phone = phone
      password.username = username
      password.password = passwordValue
      password.archived = false
      manager.password = password
    } else {
      manager.password?.title = title
      manager.password?.url = url
      manager.password?.email = email
      manager.password?.phone = phone
      manager.password?.username = username
      manager.password?.password = passwordValue
    }

    manager.savePassword()
  }

  fun deletePassword() {
    manager.deletePassword()
  }

  private fun discardChanges() {
    reloadData()
  }

  fun hasEditableChanges(): Boolean {
    val title = binding.titleEditText.text.toString()
    val url = binding.urlEditText.text.toString()
    val email = binding.emailEditText.text.toString()
    val phone = binding.phoneEditText.text.toString()
    val username = binding.usernameEditText.text.toString()
    val passwordValue = binding.passwordEditText.text.toString()

    return if (passwordId == null) {
      !(title.isEmpty() && url.isEmpty() && email.isEmpty() && phone.isEmpty() && username.isEmpty() && passwordValue.isEmpty())
    } else {
      !(title == manager.password?.title && url == manager.password?.url && email == manager.password?.email && phone == manager.password?.phone && username == manager.password?.username && passwordValue == manager.password?.password)
    }
  }

  private fun setArgs() {
    passwordId = arguments?.get(PARAM_PASSWORD_ID) as? String
  }

  private fun setDependencies(savedInstanceState: Bundle?) {
    manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
      Shared.store?.retrieve(TAG, "manager") as PasswordEditorFragmentManager
    } else {
      context?.let {
        PasswordEditorFragmentManagerBuilder().set(context = activity).set(listener = this).build()
      }!!
    }
  }

  private fun fetchPassword() {
    this.passwordId?.let {
      val getPasswordTask = GetPasswordByIdTask(it, Shared.getReadableDatabase(activity))
      Shared.serialTaskExecutor?.exec(getPasswordTask, this)
    }
  }

  private fun updateUiState() {
    binding.titleEditText.isEnabled = true
    binding.urlEditText.isEnabled = true
    binding.emailEditText.isEnabled = true
    binding.phoneEditText.isEnabled = true
    binding.usernameEditText.isEnabled = true
    binding.passwordEditText.isEnabled = true

  }

  private fun reloadData() {
    val password = manager.password

    binding.titleEditText.setText(password?.title, TextView.BufferType.EDITABLE)
    binding.urlEditText.setText(password?.url, TextView.BufferType.EDITABLE)
    binding.emailEditText.setText(password?.email, TextView.BufferType.EDITABLE)
    binding.phoneEditText.setText(password?.phone, TextView.BufferType.EDITABLE)
    binding.usernameEditText.setText(password?.username, TextView.BufferType.EDITABLE)
    binding.passwordEditText.setText(password?.password, TextView.BufferType.EDITABLE)
  }

  override fun onClick(view: View?) {
    when (view) {
      binding.copyButton -> { // Todo: Add copy functionality
      }
      binding.revealPasswordButton -> {
        if (revealPassword) {
          binding.passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
          binding.revealPasswordButton.text = getString(R.string.password_editor_button_title_reveal_password)
          revealPassword = false
        } else {
          binding.passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
          binding.revealPasswordButton.text = getString(R.string.password_editor_button_title_hide_password)
          revealPassword = true
        }
      }
    }
  }

  override fun onTaskStarted(task: Task) {

  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is GetPasswordByIdTask -> {
        task.result?.password?.let {
          manager.password = it

          updateUiState()
          reloadData()
        }
      }
    }
  }

  override fun onPasswordSavingStarted(
    password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
  ) {

  }

  override fun onPasswordSaved(password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?) {
    listener?.get()?.onPasswordSaved(password, WeakReference(this))
  }

  override fun onPasswordSavingFailed(
    password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
  ) {
    listener?.get()?.onPasswordSavingFailed(
      password, "Error: Could not save password.", WeakReference(this)
    )
  }

  override fun onPasswordDeletionStarted(
    password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
  ) {

  }

  override fun onPasswordDeleted(password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?) {
    listener?.get()?.onPasswordDeleted(password, WeakReference(this))
  }

  override fun onPasswordDeletionFailed(
    password: PasswordEntity, manager: WeakReference<PasswordEditorFragmentManager>?
  ) {
    listener?.get()?.onPasswordDeletionFailed(
      password, "Error: Could not delete password.", WeakReference(this)
    )
  }

  companion object {

    @JvmField
    val TAG: String = this::class.java.name

    const val PARAM_PASSWORD_ID = "PARAM_PASSWORD_ID"
  }
}