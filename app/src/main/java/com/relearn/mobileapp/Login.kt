package com.relearn.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ActionTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shobhitpuri.custombuttons.GoogleSignInButton

class Login : AppCompatActivity() {

    companion object{
        const val CONST_SIGN_IN=34
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var login: GoogleSignInButton
    private lateinit var googleAuth:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_login)


        auth= FirebaseAuth.getInstance()
        login=findViewById(R.id.sign_in_button)
        login.setOnClickListener {
            Log.d("Main","Sign in button clicked")
            GoogleSignIN()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("215507932515-najlb5p6eohdrdpt688d6rclpf2eovi9.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleAuth = GoogleSignIn.getClient(this, gso)


         //Image Slider
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider) // init imageSlider

        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel(R.drawable.img3, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.img2, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.img1, ScaleTypes.CENTER_INSIDE))

        imageSlider.setImageList(imageList)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here.
            }
        })

        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                //println("Pos: " + position)
            }
        })

        imageSlider.setTouchListener(object : TouchListener {
            override fun onTouched(touched: ActionTypes) {
                if (touched == ActionTypes.DOWN){
                    imageSlider.stopSliding()
                } else if (touched == ActionTypes.UP ) {
                    imageSlider.startSliding(1000)
                }
            }
        })

    }


    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, Details::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Snackbar.make(login,"Login Failed.. Try Again",Snackbar.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    private fun GoogleSignIN() {
        //val account=GoogleSignIn.getLastSignedInAccount(this)
        val account = FirebaseAuth.getInstance().currentUser
        if(account==null){
            val signInIntent=googleAuth.signInIntent
            startActivityForResult(signInIntent, CONST_SIGN_IN)
        }
        else{
            startActivity(Intent(this,SubjectDashboard::class.java))
            finish()
            Log.d("Main","Acc is not null -> " + account.displayName.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CONST_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val accout=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(accout.idToken)
            }
            catch (e:ApiException){
                Toast.makeText(applicationContext,"Login Failed.. Try Again",Toast.LENGTH_SHORT).show()
                Log.d("Main",e.toString())
            }
        }
    }
}
