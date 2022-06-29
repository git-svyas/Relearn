package com.example.mobileapp

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeHelloAnim: LottieAnimationView
    private lateinit var startButtonAnim: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeHelloAnim = findViewById(R.id.hello_welcome_anim)
        startButtonAnim = findViewById(R.id.start_button_welcome_anim)

        startButtonAnim.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SubjectDashboard::class.java))
            finish()
        })
        welcomeHelloAnim.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                startButtonAnim.visibility = View.VISIBLE
                startButtonAnim.playAnimation()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })
    }
}