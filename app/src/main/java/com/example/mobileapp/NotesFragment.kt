package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.adapter.NotesAdapter
import com.example.mobileapp.helper.NotesHelper
import com.example.mobileapp.model.Notes
import com.facebook.shimmer.ShimmerFrameLayout

class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var parentActivity: NotesDashboard
    var notesAdapter: NotesAdapter? = null;

    private var mList: ArrayList<Notes> = ArrayList()
    private lateinit var subject: String

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

        subject = parentActivity.intent.getStringExtra("EXTRA_SUBJECT").toString()

        notesRecyclerView.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        notesRecyclerView.adapter = notesAdapter



        var timeLeft = NotesHelper.initTimer(6000)
        //NotesHelper.applyNotesList(subject,mList,timeLeft)
        NotesHelper.applyNotesByFilterList(subject, listOf(1,2,3,4,5),mList,timeLeft)


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

    private fun initTimer(duration: Int): ArrayList<Int> {
        var timeLeft: ArrayList<Int> = ArrayList(1)
        timeLeft.add(duration)
        var timer = object: CountDownTimer(duration.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                timeLeft[0] = -10
            }
        }
        timer.start()
        return timeLeft
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
        intent.putExtra("EXTRA_NOTE_ID", notesAdapter?.notesList?.get(position)?.id)
        intent.putExtra("EXTRA_VIEWS", notesAdapter?.notesList?.get(position)?.views.toString())
        startActivity(intent)
    }

    override fun onLongClick(position: Int) {
        Log.d("Main","onlong click ${position}")

        val longurl = notesAdapter?.notesList?.get(position)?.url.toString()
        val noteTitle = notesAdapter?.notesList?.get(position)?.name.toString()
        Log.d("Main","Data is long url $longurl and noteTitle is $noteTitle")
        var shortUrl: ArrayList<String> = ArrayList(1)
        shortUrl.add("Unavailable")
        NotesHelper.getShortenLink(longurl,shortUrl)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND


        var timeLeft = 3e8

        while(timeLeft > 0 && shortUrl[0] == "Unavailable"){
            timeLeft--;
        }

        Log.d("Main","Timeleft outside loop ${timeLeft}")
        Log.d("Main","short url outside loop ${shortUrl[0]}")

        // Setting up the message
        val noteShareText: String = "You can download \n${noteTitle} pdf here - ${shortUrl[0]}\n" +
                "\n" +
                "Want full access to all college notes and past semester papers?\n" +
                "Download the Relearn App now.\n" +
                "Available on the Google Playstore!"

        var noteShareDefaultText: String = "You can download \n${noteTitle} pdf from the Relearn App\n" +
                "\n" +
                "Download the App now.\n" +
                "Available on the Google Playstore!"


        if(timeLeft == 0.0){
            shareIntent.putExtra(Intent.EXTRA_TEXT,noteShareDefaultText)
        }
        else{
            shareIntent.putExtra(Intent.EXTRA_TEXT,noteShareText)
        }
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent,"Share via"))
    }

}