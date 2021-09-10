package com.ankur.securenotes.ui.fragment.password.viewer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankur.securenotes.databinding.FragmentPasswordListBinding
import com.ankur.securenotes.databinding.FragmentPasswordViewerBinding
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.task.GetPasswordByIdTask
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.ui.common.adapter.ListViewableRecyclerViewAdapter
import com.ankur.securenotes.ui.common.adapter.PasswordListRecycleViewAdapter
import com.ankur.securenotes.ui.common.adapter.PasswordViewerRecyclerViewAdapter
import com.ankur.securenotes.ui.common.viewholder.listviewable.ListViewable
import com.ankur.securenotes.ui.fragment.password.editor.PasswordEditorFragment
import com.ankur.securenotes.ui.fragment.password.editor.PasswordEditorFragmentManager
import com.ankur.securenotes.ui.fragment.password.editor.PasswordEditorFragmentManagerBuilder
import com.ankur.securenotes.ui.fragment.password.list.PasswordListFragment
import com.ankur.securenotes.ui.fragment.password.list.PasswordListFragmentManager
import com.ankur.securenotes.ui.fragment.password.list.PasswordListFragmentManagerBuilder

class PasswordViewerFragment : Fragment(), SerialTaskExecutor.Listener {

  private lateinit var binding: FragmentPasswordViewerBinding
  private lateinit var activity: Activity
  private lateinit var adapter: ListViewableRecyclerViewAdapter
  private lateinit var manager: PasswordViewerFragmentManager

  private var passwordId: String? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)

    activity = context as Activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setArgs()
    setDependencies(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = FragmentPasswordViewerBinding.inflate(layoutInflater, container, false)
    val view = binding.root

    setupRecyclerView()

    return view
  }

  override fun onStart() {
    super.onStart()

    fetchPassword()
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

  private fun setupRecyclerView() {
    val linearLayoutManager = LinearLayoutManager(activity)
    binding.recyclerView.layoutManager = linearLayoutManager
    adapter = PasswordViewerRecyclerViewAdapter(null)
    binding.recyclerView.adapter = adapter
  }

  private fun setArgs() {
    passwordId = arguments?.get(PasswordEditorFragment.PARAM_PASSWORD_ID) as? String
  }

  private fun setDependencies(savedInstanceState: Bundle?) {
    manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
      Shared.store?.retrieve(TAG, "manager") as PasswordViewerFragmentManager
    } else {
      context?.let {
        PasswordViewerFragmentManagerBuilder().set(context = activity).build()
      }!!
    }
  }

  private fun fetchPassword() {
    this.passwordId?.let {
      val getPasswordTask = GetPasswordByIdTask(it, Shared.getReadableDatabase(activity))
      Shared.serialTaskExecutor?.exec(getPasswordTask, this)
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun reloadData() {
    binding.titleTextView.text = manager.password?.title

    val listOfListViewable = mutableListOf<ListViewable>()

    if (!manager.password?.url.isNullOrEmpty()) {
      listOfListViewable.add(ListViewable("Url", manager.password?.url))
    }

    if (!manager.password?.username.isNullOrEmpty()) {
      listOfListViewable.add(ListViewable("Username", manager.password?.username))
    }

    if (!manager.password?.email.isNullOrEmpty()) {
      listOfListViewable.add(ListViewable("Email", manager.password?.email))
    }

    if (!manager.password?.phone.isNullOrEmpty()) {
      listOfListViewable.add(ListViewable("Phone", manager.password?.phone))
    }

    if (!manager.password?.password.isNullOrEmpty()) {
      listOfListViewable.add(ListViewable("Password", manager.password?.password))
    }

    adapter.updateItems(listOfListViewable)
    adapter.notifyDataSetChanged()
  }

  override fun onTaskStarted(task: Task) {

  }

  override fun onTaskFinished(task: Task) {
    when (task) {
      is GetPasswordByIdTask -> {
        task.result?.password?.let {
          manager.password = it

          reloadData()
        }
      }
    }
  }

  companion object {

    @JvmField
    val TAG: String = this::class.java.name
  }
}