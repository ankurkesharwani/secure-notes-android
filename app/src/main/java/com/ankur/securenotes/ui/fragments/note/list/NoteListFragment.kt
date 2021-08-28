package com.ankur.securenotes.ui.fragments.note.list

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
import com.ankur.securenotes.databinding.FragmentNoteListBinding
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.ui.common.adapters.NoteListRecycleViewAdapter
import com.ankur.securenotes.ui.common.viewholders.NoteListItemViewHolder
import java.lang.ref.WeakReference

class NoteListFragment : Fragment(), NoteListFragmentManager.Listener, NoteListItemViewHolder.Listener {

  interface Listener {
    fun onNoteItemSelected(note: NoteEntity, fragment: WeakReference<Fragment>)
  }

  private lateinit var binding: FragmentNoteListBinding
  private lateinit var activity: Activity
  private lateinit var adapter: NoteListRecycleViewAdapter
  private lateinit var manager: NoteListFragmentManager

  private var listener: WeakReference<Listener>? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)

    activity = context as Activity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    manager = if (savedInstanceState?.getBoolean("isChangingConfiguration") == true) {
      Shared.store?.retrieve(TAG, "manager") as NoteListFragmentManager
    } else {
      context?.let {
        NoteListFragmentManagerBuilder().set(context = activity).set(listener = this).build()
      }!!
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = FragmentNoteListBinding.inflate(layoutInflater, container, false)
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
    adapter = NoteListRecycleViewAdapter(this)
    binding.recyclerView.adapter = adapter
  }

  private fun setupSwipeToRefresh() {
    binding.swipeToRefresh.setColorSchemeResources(
      R.color.refresh_progress_1, R.color.refresh_progress_2
    )

    binding.swipeToRefresh.setOnRefreshListener { fetchData() }
  }

  private fun fetchData() {
    manager.fetchNoteList()
  }

  @SuppressLint("NotifyDataSetChanged")
  private fun reloadData() {
    adapter.updateNotes(manager.notes)
    adapter.notifyDataSetChanged()
  }

  override fun onNoteListFetchStart(manager: NoteListFragmentManager?) {
    binding.swipeToRefresh.isRefreshing = true
  }

  override fun onNoteListFetched(notes: List<NoteEntity>?, manager: NoteListFragmentManager?) {
    binding.swipeToRefresh.isRefreshing = false
    reloadData()
  }

  override fun onNoteListFetchFailed(
    errorCode: Int?, message: String?, manager: NoteListFragmentManager?
  ) {
    binding.swipeToRefresh.isRefreshing = false
    reloadData()
    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
  }

  override fun onNoteItemClicked(note: NoteEntity, viewHolder: RecyclerView.ViewHolder) {
    listener?.get()?.onNoteItemSelected(note, WeakReference(this))
  }

  companion object {

    @JvmField
    val TAG: String = this::class.java.name
  }
}