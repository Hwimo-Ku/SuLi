package com.example.suli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.suli.databinding.ActivityLogoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class LogOutActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLogoutBinding
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseauth = FirebaseAuth.getInstance()


        binding.buttonLogOut.setOnClickListener {
            firebaseauth.signOut()
            //구글 로그아웃은 왜 안되징
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}