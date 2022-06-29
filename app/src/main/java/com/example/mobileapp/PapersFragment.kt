package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.adapter.PapersAdapter
import com.example.mobileapp.helper.NotesHelper
import com.example.mobileapp.model.Notes

class PapersFragment : Fragment(), PapersAdapter.OnItemClickListener {

    private lateinit var papersRecyclerView: RecyclerView
    private lateinit var parentActivity: NotesDashboard
    var papersAdapter: PapersAdapter? = null

    private var mList: ArrayList<Notes> = ArrayList()
    private lateinit var subject: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_papers, container, false)
        NotesHelper.setNotesFragment(this)
        NotesHelper.mPapersFragement = this

        parentActivity = activity as NotesDashboard
        subject = parentActivity.intent.getStringExtra("EXTRA_SUBJECT").toString()

        papersRecyclerView = view.findViewById(R.id.rv_papers)

        //edge case
        parentActivity.chipGroup.visibility = View.GONE

        papersAdapter = PapersAdapter(this)

        papersRecyclerView.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        papersRecyclerView.adapter = papersAdapter
        Log.d("Main","Inside Papers Fragment")

        var timeLeft = NotesHelper.initTimer(6000)
        NotesHelper.applyNotesByFilterList(subject, arrayListOf(0),mList,timeLeft)


        var state: Int? = null
        papersRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
        //parentActivity.chipGroup.visibility = View.GONE
    }

    private fun show() {
        parentActivity.frameLayout.visibility = View.VISIBLE
        //parentActivity.chipGroup.visibility = View.VISIBLE
    }

    //Recycler view Item click
    override fun onClick(position: Int) {
        Log.d("Main","Paper -> Item clicked ${position}")
        var intent: Intent = Intent(parentActivity,PdfActivity::class.java)
        intent.putExtra("EXTRA_URL", papersAdapter?.papersList?.get(position)?.url)
        intent.putExtra("EXTRA_NOTE_ID", papersAdapter?.papersList?.get(position)?.id)
        intent.putExtra("EXTRA_VIEWS", papersAdapter?.papersList?.get(position)?.views.toString())
        startActivity(intent)
    }
}