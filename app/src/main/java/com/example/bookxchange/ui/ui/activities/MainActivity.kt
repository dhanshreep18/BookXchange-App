package com.example.bookxchange.ui.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.bookxchange.R
import com.example.bookxchange.utils.Constants

class MainActivity : AppCompatActivity() {

    lateinit var tv_main: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_main=findViewById(R.id.tv_main)

        val sharedPreferences =
                getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        tv_main.text= "The logged in user is $username."


    }
}