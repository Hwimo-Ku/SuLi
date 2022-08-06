package com.example.suli

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.suli.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseauth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSignUp.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                }
                else {
                    Toast.makeText(this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "주어진 칸을 모두 입력해주세요", Toast.LENGTH_SHORT).show()

            }
        }
    }
}