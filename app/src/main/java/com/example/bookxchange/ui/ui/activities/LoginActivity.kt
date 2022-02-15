package com.example.bookxchange.ui.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.example.bookxchange.R
import com.example.bookxchange.firestore.FirestoreClass
import com.example.bookxchange.models.User
import com.example.bookxchange.utils.Constants
import com.example.bookxchange.utils.MSPButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener{

    lateinit var tv_register:TextView
    lateinit var et_email:EditText
    lateinit var et_password:EditText
    lateinit var tv_forgot_password:TextView
    lateinit var btn_login: MSPButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_register=findViewById(R.id.tv_register)
        et_email=findViewById(R.id.et_email)
        et_password=findViewById(R.id.et_password)
        tv_forgot_password=findViewById(R.id.tv_forgot_password)
        btn_login=findViewById(R.id.btn_login)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        tv_forgot_password.setOnClickListener(this)

        btn_login.setOnClickListener(this)

        tv_register.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            FirestoreClass().getUserDetails(this@LoginActivity)
                            //showErrorSnackBar("You are logged in successfully.", false)
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
        }
    }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

}