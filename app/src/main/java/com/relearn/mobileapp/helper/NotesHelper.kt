package com.relearn.mobileapp.helper

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.relearn.mobileapp.NotesFragment
import com.relearn.mobileapp.PapersFragment
import com.relearn.mobileapp.model.Notes
import com.relearn.mobileapp.service.NotesService
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.thauvin.erik.bitly.Bitly

@Keep
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

        val bitly = Bitly("f14824a9789af86ba8bbca2157d11da09c76440f")

        @JvmStatic
        fun getShortenLink(longLink: String, shortLink: ArrayList<String>){
            GlobalScope.launch {
                shortLink[0] = bitly.bitlinks().shorten(longLink)
            }
        }

        @JvmStatic
        fun applyNotesList(subject: String, list: ArrayList<Notes>, timeLeft: ArrayList<Int>){
            //on background thread
            GlobalScope.launch {
                notesService?.getNotesBySubject(subject,list)

                var isSuccessful: Boolean = true
                while(list.size == 0){
                   if(timeLeft[0] <= 0){
                       isSuccessful = false
                       break
                   }
                }
                Log.d("Main","Api call is $isSuccessful")
                setListOnMainThread(list,isSuccessful)
            }
        }

        @JvmStatic
        fun applyNotesByFilterList(subject: String, filter: List<Int>, list: ArrayList<Notes>, timeLeft: ArrayList<Int>){
            //on background thread
            GlobalScope.launch {
                notesService?.getNotesByFilter(subject,filter, list)

                var isSuccessful: Boolean = true
                while(list.size == 0){
                    Log.d("Main","Loading...")
                    if(timeLeft[0] <= 0){
                        isSuccessful = false
                        break
                    }
                }
                Log.d("Main","Size of the list is ${list.size}")
                setListOnMainThread(list,true)
            }
        }

        @JvmStatic
        fun initTimer(duration: Int): ArrayList<Int> {
            var timeLeft: ArrayList<Int> = ArrayList(1)
            timeLeft.add(duration)
            var timer = object: CountDownTimer(duration.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft[0] = (millisUntilFinished / 1000).toInt()
                    Log.d("Main","Time left $timeLeft")
                }
                override fun onFinish() {

                }
            }
            timer.start()
            return timeLeft
        }

        @JvmStatic
        fun setNotesFragment(fragement: Fragment){
            this.fragemnt = fragement
        }

        @JvmStatic
        fun getNotesFragment(): Fragment? {
            return fragemnt
        }

        private suspend fun setListOnMainThread(list: ArrayList<Notes>, isSuccess: Boolean) {
            withContext(Dispatchers.Main){
                //logic
                setListOnRecyclerView(list, isSuccess)
            }
        }
        private fun setListOnRecyclerView(list: ArrayList<Notes>, isSuccess: Boolean) {
            if(!isSuccess){
                Log.d("Main","Api call was not succesfull")
                Snackbar.make(notesShimmer!!,"Check Your Internet Connection",Snackbar.LENGTH_INDEFINITE)
                    .show()

            }
            else{
                if(notesRecyclerView != null){
                    notesShimmer?.stopShimmer()
                    notesShimmer?.visibility = View.GONE
                    notesRecyclerView?.visibility = View.VISIBLE
                    notesRecyclerView = null
                    completeNotesList = list
                }

                if(list.size != 0){
                    if(fragemnt is NotesFragment){
                        (fragemnt as NotesFragment)?.notesAdapter?.submitList(list)
                        Log.d("Main","setting data on notes recycler view")
                    }
                    else{
                        (fragemnt as PapersFragment)?.papersAdapter?.submitList(list)
                        Log.d("Main","setting data on papers recycler view")
                    }
                }
                else{
                    Snackbar.make(notesShimmer!!,"Check you connection, Try Again",Snackbar.LENGTH_INDEFINITE)
                        .show()
                }
            }
        }
    }


}