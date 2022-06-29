package com.example.mobileapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class PdfActivity : AppCompatActivity() {

    lateinit var pdfViewer: PDFView
    lateinit var pdfLoadingAnim: LottieAnimationView

    private val db = FirebaseFirestore.getInstance()
    private val NOTES_COLLECTION = "notes"
    private val notesCollectionRef = db.collection(NOTES_COLLECTION)
    private var documentId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfViewer = findViewById(R.id.pdf_viewer)
        pdfLoadingAnim = findViewById(R.id.pdf_loading_anim)
        val url: String? = intent.getStringExtra("EXTRA_URL")
        val views: Int? = intent.getStringExtra("EXTRA_VIEWS")?.toInt()
        Log.d("Main","views is $views in activity")
        documentId = intent.getStringExtra("EXTRA_NOTE_ID")
        if(url == null){
            Log.d("Main", "url from the intent is null")
        }
        else{
            loadPdf(url,views)
        }

    }

    private fun loadPdf(link: String, views: Int?){
        GlobalScope.launch {
            var inputStream: InputStream? = null
            var url: URL = URL(link)

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            if(connection.responseCode == 200){
                inputStream = BufferedInputStream(connection.inputStream)
            }
            Log.d("Main","Input Steam is ${inputStream}")
            pdfViewer.fromStream(inputStream)
                //.defaultPage(1)
                .onLoad(OnLoadCompleteListener {
                    Log.d("Main","Page: ${it} is loaded")
                })
                .onRender {
                    Log.d("Main","Document id is ${documentId}")
                    Log.d("Main","views is $views")
                    if (views != null) {
                        notesCollectionRef.document(documentId.toString()).update("views",views+1)
                    }
                    Log.d("Main","Document Rendedred ${it}")
                    pdfLoadingAnim.cancelAnimation()
                    pdfLoadingAnim.visibility = View.GONE
                }
                //.nightMode(true)
                .load()
        }
    }
}