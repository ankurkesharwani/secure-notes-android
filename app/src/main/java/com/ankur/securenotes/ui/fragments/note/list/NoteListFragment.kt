package com.ankur.securenotes.ui.fragments.note.list

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.shared.Shared
import com.ankur.securenotes.ui.common.adapters.NoteListRecyclerViewAdapter
import com.ankur.securenotes.ui.common.viewholders.NoteListItemViewHolder
import java.lang.ref.WeakReference

class NoteListFragment :
    Fragment(),
    NoteListFragmentManager.Listener,
    NoteListItemViewHolder.Listener {

    interface Listener {
        fun onNoteItemSelected(
            note: NoteEntity,
            fragment: WeakReference<Fragment>
        )
    }

    private lateinit var activity: Activity

    private lateinit var adapter: NoteListRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
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
                NoteListFragmentManagerBuilder()
                    .set(context = activity)
                    .set(listener = this)
                    .build()
            }!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

        setupRecyclerView()
        setupSwipeToRefresh()

        return view
    }

    /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    */

    override fun onStart() {
        super.onStart()

        fetchData()
    }

    override fun onResume() {
        super.onResume()

        reloadData()
    }

    /*
    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
    }
    */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (activity.isChangingConfigurations) {
            outState.putBoolean("isChangingConfiguration", true)
            Shared.store!!.save(TAG, "manager", manager)
        } else {
            outState.putBoolean("isChangingConfiguration", false)
        }
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDetach() {
        super.onDetach()
    }
    */

    fun setListener(listener: Listener) {
        this.listener = WeakReference(listener)
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        adapter = NoteListRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun setupSwipeToRefresh() {
        swipeToRefresh.setColorSchemeResources(
            R.color.refresh_progress_1,
            R.color.refresh_progress_2)

        swipeToRefresh.setOnRefreshListener {
            fetchData()
        }
    }

    private fun fetchData() {
        manager.fetchNoteList()
    }

    private fun reloadData() {
        adapter.updateNotes(manager.notes)
        adapter.notifyDataSetChanged()
    }

    override fun onNoteListFetchStart(manager: NoteListFragmentManager?) {
        swipeToRefresh.isRefreshing = true
    }

    override fun onNoteListFetched(
        notes: List<NoteEntity>?,
        manager: NoteListFragmentManager?
    ) {
        swipeToRefresh.isRefreshing = false
        reloadData()
    }

    override fun onNoteListFetchFailed(
        errorCode: Int?,
        message: String?,
        manager: NoteListFragmentManager?
    ) {
        swipeToRefresh.isRefreshing = false
        reloadData()
        Toast.makeText(activity, message, Toast.LENGTH_LONG)
            .show()
    }

    override fun onNoteItemClicked(
        note: NoteEntity,
        viewHolder: RecyclerView.ViewHolder
    ) {
        listener?.get()
            ?.onNoteItemSelected(note, WeakReference(this))
    }

    companion object {

        @JvmField
        val TAG = this::class.java.name
    }
}