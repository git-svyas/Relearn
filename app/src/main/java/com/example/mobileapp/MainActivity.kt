package com.example.mobileapp


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.Login
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        val name = getSharedPreferences(getString(R.string.user_details_sf), MODE_PRIVATE)
            .getString("STUDENT_NAME",null);

        Handler().postDelayed({
            if(user != null){
                if(name == null){
                    val detailsIntent = Intent(this, Details::class.java)
                    startActivity(detailsIntent)
                }
                else {
                    val subjectDashboardIntent = Intent(this, SubjectDashboard::class.java)
                    startActivity(subjectDashboardIntent)
                }
            }
            else{
                val signInIntent = Intent(this,Login::class.java)
                startActivity(signInIntent)
            }
            finish()
        },600)
    }

}