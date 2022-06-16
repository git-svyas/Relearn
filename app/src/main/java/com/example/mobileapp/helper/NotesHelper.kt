package com.example.mobileapp.helper

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.NotesFragment
import com.example.mobileapp.PapersFragment
import com.example.mobileapp.model.Notes
import com.example.mobileapp.repository.NotesRepository
import com.example.mobileapp.service.NotesService
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesHelper {

    companion object{
        @JvmStatic
        var completeNotesList: List<Notes>? = null

        private var fragemnt: Fragment? = null
        @JvmStatic
        var mNotesFragement: NotesFragment? = null
        @JvmStatic
        var mPapersFragement: PapersFragment? = null
        private var notesService: NotesService = NotesService()
        @JvmStatic
        var notesShimmer: ShimmerFrameLayout? = null
        @JvmStatic
        var notesRecyclerView: RecyclerView? = null

        @JvmStatic
        fun applyNotesList(subject: String, list: ArrayList<Notes>){
            //on background thread
            GlobalScope.launch {
                notesService?.getNotesBySubject(subject,list)

                while(list.size == 0){
                    Log.d("Main","Loading...")
                }
                setListOnMainThread(list)
            }
        }

        @JvmStatic
        fun applyNotesByFilterList(subject: String, filter: List<Int>, list: ArrayList<Notes>){
            //on background thread
            GlobalScope.launch {
                notesService?.getNotesByFilter(subject,filter, list)
                while(list.size == 0){
                    Log.d("Main","Loading...")

                }
                Log.d("Main","Size of the list is ${list.size}")
                setListOnMainThread(list)
            }
        }

        @JvmStatic
        fun setNotesFragment(fragement: Fragment){
            this.fragemnt = fragement
        }

        @JvmStatic
        fun getNotesFragment(): Fragment? {
            return fragemnt
        }

        private suspend fun setListOnMainThread(list: ArrayList<Notes>) {
            withContext(Dispatchers.Main){
                //logic
                setListOnRecyclerView(list)
            }
        }
        private fun setListOnRecyclerView(list: ArrayList<Notes>) {
            if(notesRecyclerView != null){
                notesShimmer?.stopShimmer()
                notesShimmer?.visibility = View.GONE
                notesRecyclerView?.visibility = View.VISIBLE
                notesRecyclerView = null
                completeNotesList = list
            }

            if(fragemnt is NotesFragment){
                (fragemnt as NotesFragment)?.notesAdapter?.submitList(list)
                Log.d("Main","setting data on notes recycler view")
            }
            else{
                (fragemnt as PapersFragment)?.papersAdapter?.submitList(list)
                Log.d("Main","setting data on papers recycler view")
            }
        }
    }


}