package com.ankur.securenotes.ui.fragments.password.list

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankur.securenotes.R
import com.ankur.securenotes.databinding.FragmentPasswordListBinding
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.ui.common.adapters.PasswordListRecycleViewAdapter
import com.ankur.securenotes.ui.common.viewholders.PasswordListItemViewHolder
import java.lang.ref.WeakReference

class PasswordListFragment : Fragment(), PasswordListFragmentManager.Listener, PasswordListItemViewHolder.Listener {

  interface Listener {
    fun onPasswordItemSelected(password: PasswordEntity, fragment: WeakReference<Fragment>)
  }

  private lateinit var binding: FragmentPasswordListBinding
  private lateinit var activity: Activity
  private lateinit var adapter: PasswordListRecycleViewAdapter
  private lateinit var manager: PasswordListFragmentManager

  private var listener: WeakReference<Listener>? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)

    activity = context as Activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
      Shared.store?.retrieve(TAG, "manager") as PasswordListFragmentManager
    } else {
      context?.let {
        PasswordListFragmentManagerBuilder().set(context = activity).set(listener = this).build()
      }!!
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = FragmentPasswordListBinding.inflate(layoutInflater, container, false)
    val view = binding.root

    setupRecyclerView()
    setupSwipeToRefresh()

    return view
  }

  override fun onStart() {
    super.onStart()

    fetchData()
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

  private fun setupRecyclerView() {
    val linearLayoutManager = LinearLayoutManager(activity)
    binding.recyclerView.layoutManager = linearLayoutManager
    adapter = PasswordListRecycleViewAdapter(this)
    binding.recyclerView.adapter = adapter
  }

  private fun setupSwipeToRefresh() {
    binding.swipeToRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2)
    binding.swipeToRefresh.setOnRefreshListener { fetchData() }
  }

  private fun fetchData() {
    manager.fetchPasswordList()
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun reloadData() {
    adapter.updatePasswords(manager.passwords)
    adapter.notifyDataSetChanged()
  }

  override fun onPasswordListFetchStart(manager: PasswordListFragmentManager?) {
    binding.swipeToRefresh.isRefreshing = true
  }

  override fun onPasswordListFetched(passwords: List<PasswordEntity>?, manager: PasswordListFragmentManager?) {
    binding.swipeToRefresh.isRefreshing = false
    reloadData()
  }

  override fun onPasswordListFetchFailed(errorCode: Int?, message: String?, manager: PasswordListFragmentManager?) {
    binding.swipeToRefresh.isRefreshing = false
    reloadData()
    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
  }

  override fun onPasswordItemClicked(password: PasswordEntity, viewHolder: RecyclerView.ViewHolder) {
    listener?.get()?.onPasswordItemSelected(password, WeakReference(this))
  }

  companion object {

    @JvmField
    val TAG: String = this::class.java.name
  }
}