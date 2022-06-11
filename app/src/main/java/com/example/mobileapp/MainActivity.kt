package com.example.mobileapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.mobileapp.model.Filters
import com.example.mobileapp.model.Notes
import com.example.mobileapp.service.NotesService
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    var notesService: NotesService? = NotesService()
    lateinit var tv: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.button)
        //getNotesList("CN")
        getNotesByFilterList("CN", arrayListOf(Filters.HANDWITTEN.ordinal, Filters.IMPORTANT.ordinal))

    }

    private fun getNotesList(subject: String){
        //on background thread
        GlobalScope.launch {
            val list: List<Notes> = ArrayList();
            notesService?.getNotesBySubject(subject,list)
            Log.d("Main","Size of the list is ${list.size}")

            while(list.size == 0){
                //loading
            }

            //update list on the main thread
            Log.d("Main","Size of the list is ${list.size}")
            setListOnMainThread(list)
        }
    }

    private fun getNotesByFilterList(subject: String, filter: List<Int>){
        //on background thread
        GlobalScope.launch {
            val list: List<Notes> = ArrayList();
            notesService?.getNotesByFilter(subject,filter, list)
            Log.d("Main","Size of the Filter list is ${list.size}")

            while(list.size == 0){
                //loading
            }

            //update list on the main thread
            Log.d("Main","Size of the Filter list is ${list.size}")
            setListOnMainThread(list)
        }
    }

    private suspend fun setListOnMainThread(list: List<Notes>) {
        withContext(Main){
            //logic
            var res = ""
            for(item in list){
                Log.d("Main","Topic name ${item.name}")
                res += (item.name + "\n")
            }
            tv.text = res
        }
    }


}