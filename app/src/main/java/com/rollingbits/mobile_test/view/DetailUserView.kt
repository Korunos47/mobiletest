package com.rollingbits.mobile_test.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rollingbits.mobile_test.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detailuserview.*

class DetailUserView: AppCompatActivity() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var avatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailuserview)

        firstName = intent.getStringExtra("first_name")
        lastName = intent.getStringExtra("last_name")
        email = intent.getStringExtra("email")
        avatar = intent.getStringExtra("avatar")


        firstnameTV.text = firstName
        lastnameTV.text = lastName
        emailTV.text = email

        if(checkInternetConnection(this))
            if(avatar.isNotEmpty()) {
                Picasso.get().load(avatar).into(avatarIV)
            }
    }

    private fun checkInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}