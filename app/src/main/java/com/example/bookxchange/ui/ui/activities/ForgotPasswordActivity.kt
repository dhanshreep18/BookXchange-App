package com.example.bookxchange.ui.ui.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.bookxchange.R
import com.example.bookxchange.utils.MSPButton
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var btn_submit: MSPButton
    lateinit var et_email_forgot_pw: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        toolbar=findViewById(R.id.toolbar_forgot_password_activity)
        btn_submit=findViewById(R.id.btn_submit)
        et_email_forgot_pw=findViewById(R.id.et_email_forgot_pw)

        setupActionBar()

        btn_submit.setOnClickListener {

            // Get the email id from the input field.
            val email: String = et_email_forgot_pw.text.toString().trim { it <= ' ' }

            // Now, If the email entered in blank then show the error message or else continue with the implemented feature.
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {

                // Show the progress dialog.
                showProgressDialog(resources.getString(R.string.please_wait))

                // This piece of code is used to send the reset password link to the user's email id if the user is registered.
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->

                        // Hide the progress dialog
                        hideProgressDialog()

                        if (task.isSuccessful) {
                            // Show the toast message and finish the forgot password activity to go back to the login screen.
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }



}