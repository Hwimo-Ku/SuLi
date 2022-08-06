package com.example.suli

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.suli.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {
    //바인딩, 파이어베이스 관련
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseauth: FirebaseAuth
    //구글 로그인
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauth = FirebaseAuth.getInstance()

        //구글 로그인 관련
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        if(firebaseauth.currentUser != null){
            val intent = Intent(this, LogOutActivity::class.java)
            startActivity(intent)
        }
        //회원가입 페이지로 이동
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        //이메일 로그인
        binding.buttonSignIn.setOnClickListener {
            email_Sign_IN()
        }
        //구글 로그인
        binding.buttonSignInGoogle.setOnClickListener{
            signInGoogle()
        }
    }

    fun email_Sign_IN() {
        val email = binding.emailEt.text.toString()
        val pass = binding.passET.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            firebaseauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, LogOutActivity::class.java)
                    startActivity(intent)
                } else {
                    //Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "아이디와 비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "주어진 칸을 모두 채워주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent : Intent = Intent(this , LogOutActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }
}