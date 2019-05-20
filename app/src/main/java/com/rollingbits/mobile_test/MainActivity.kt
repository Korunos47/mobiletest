package com.rollingbits.mobile_test

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.File

// https://github.com/cbeust/klaxon
class MainActivity : AppCompatActivity() {
    private var offlineMode = false
    private val jsonFile = File("jsonData.json")
    val RESTUrl = "https://reqres.in/api/users?per_page=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        offlineMode = activeNetwork?.isConnectedOrConnecting == true
        if (offlineMode)
            getJSON()
    }

    private fun getJSON() {
        RESTUrl.httpGet().responseString() { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Toast.makeText(this, "Fehler beim Abruf", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    jsonFile.writeText(result.value)

                }
            }
        }
    }

    data class User(
        val page: Int,
        val per_page: Int,
        val total: Int,
        val total_pages: Int,
        val dada: List<UserData>) {

        data class UserData(
            val id: Int,
            val email: String,
            val first_name: String,
            val last_name: String,
            val avatar: String
        )
    }
}

