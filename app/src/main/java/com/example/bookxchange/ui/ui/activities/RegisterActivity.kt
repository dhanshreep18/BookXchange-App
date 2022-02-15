package com.example.bookxchange.ui.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import com.example.bookxchange.R
import com.example.bookxchange.firestore.FirestoreClass
import com.example.bookxchange.models.User
import com.example.bookxchange.utils.MSPButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity(){
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var tv_login: TextView
    lateinit var et_first_name: EditText
    lateinit var et_last_name: EditText
    lateinit var et_email: EditText
    lateinit var et_password: EditText
    lateinit var et_confirm_password: EditText
    lateinit var btn_register: MSPButton
    lateinit var cb_terms_and_condition: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toolbar=findViewById(R.id.toolbar_register_activity)
        tv_login=findViewById(R.id.tv_login)
        et_first_name=findViewById(R.id.et_first_name)
        et_last_name=findViewById(R.id.et_last_name)
        et_email=findViewById(R.id.et_email)
        et_password=findViewById(R.id.et_password)
        et_confirm_password=findViewById(R.id.et_confirm_password)
        btn_register=findViewById(R.id.btn_register)
        cb_terms_and_condition=findViewById(R.id.cb_terms_and_condition)

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
        setupActionBar()
        btn_register.setOnClickListener {
            registerUser()
        }

        tv_login.setOnClickListener {
            onBackPressed()
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

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                //showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true
            }
        }
    }

    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim { it <= ' ' },
                                et_last_name.text.toString().trim { it <= ' ' },
                                et_email.text.toString().trim { it <= ' ' }
                            )

                            FirestoreClass().registerUser(this@RegisterActivity,user)

                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            //FirebaseAuth.getInstance().signOut()
                            //finish()

                        } else {
                            // If the registering is not successful then show error message.
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }
    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
                this@RegisterActivity,
                resources.getString(R.string.register_success),
                Toast.LENGTH_SHORT
        ).show()

        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }

}