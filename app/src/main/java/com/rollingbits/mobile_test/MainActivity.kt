package com.rollingbits.mobile_test

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.beust.klaxon.Parser
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.StringBuilder

// https://github.com/cbeust/klaxon
class MainActivity : AppCompatActivity() {
    private var offlineMode = false
    private lateinit var jsonDirectory: File

    val RESTUrl = "https://reqres.in/api/users?per_page=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()
        checkInternetConnection()
    }

    private fun initialization() {
        jsonDirectory = File(filesDir, "JSONData")
    }

    private fun checkInternetConnection() {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        offlineMode = activeNetwork?.isConnectedOrConnecting == true
        if (offlineMode)
            getJSON()
    }

    private fun getJSON() {
        RESTUrl.httpGet().responseString() { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Toast.makeText(this, "Fehler beim Abruf", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    if (jsonDirectory.exists()) {
                        writeJSONFile(result.value)
                    } else { // Create directory
                        jsonDirectory.mkdirs()
                        writeJSONFile(result.value)
                    }
                }
            }
        }
        workwithjson()
    }

    private fun workwithjson() {
        val filename = File(jsonDirectory.path, "/jsonData.json")
        val inputAsString = FileInputStream(filename).bufferedReader().use { it.readText() }

        /*
        val parser: Parser = Parser.default()
        val stringBuilder = StringBuilder(inputAsString)
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        println("Name: ${json.string("page")}")
        */

        //val obj = parse(jsonDirectory.path + "/jsonData.json") as JsonObject

        val result = Klaxon().parse<User>(inputAsString)
        print(result)
    }

    private fun writeJSONFile(data: String) {
        val file = File(jsonDirectory, "/jsonData.json")
        FileOutputStream(file).use {
            it.write(data.toByteArray())
        }
    }

    fun parse(name: String): Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser.default().parse(inputStream)
        }
    }

    data class User(
        val page: Int,
        val per_page: Int,
        val total: Int,
        val total_pages: Int,
        val data: List<UserData>
    )

    data class UserData(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val avatar: String
    )

}

