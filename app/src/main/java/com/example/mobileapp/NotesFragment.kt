package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.adapter.NotesAdapter
import com.example.mobileapp.helper.NotesHelper
import com.example.mobileapp.model.Notes
import com.example.mobileapp.service.NotesService
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var parentActivity: NotesDashboard
    var notesAdapter: NotesAdapter? = null;

    private var mList: ArrayList<Notes> = ArrayList()
    private val subject: String = "OS"

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout)
        notesRecyclerView = view.findViewById(R.id.rv_notes)

        shimmerFrameLayout.startShimmer()

        NotesHelper.setNotesFragment(this)
        NotesHelper.mNotesFragement = this
        NotesHelper.notesShimmer = shimmerFrameLayout
        NotesHelper.notesRecyclerView = notesRecyclerView

        parentActivity = activity as NotesDashboard
        notesAdapter = NotesAdapter(this)
        notesAdapter

        notesRecyclerView.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        notesRecyclerView.adapter = notesAdapter

        NotesHelper.applyNotesList(subject,mList)


        var state: Int? = null
        notesRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                state = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 && (state == 0 || state == 2)){
                    hide()
                }
                else if(dy < -10){
                    show()
                }
            }
        })
        return view
    }


    private fun hide() {
        parentActivity.frameLayout.visibility = View.GONE
        parentActivity.chipGroup.visibility = View.GONE
    }

    private fun show() {
        parentActivity.frameLayout.visibility = View.VISIBLE
        parentActivity.chipGroup.visibility = View.VISIBLE
    }

    //Recyclerview Item click
    override fun onClick(position: Int) {
        Log.d("Main","Item clicked ${position}")
        var intent: Intent = Intent(parentActivity,PdfActivity::class.java)
        intent.putExtra("EXTRA_URL", notesAdapter?.notesList?.get(position)?.url)
        startActivity(intent)
    }


}