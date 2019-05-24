package com.rollingbits.mobile_test.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.rollingbits.mobile_test.R
import com.rollingbits.mobile_test.controller.RecyclerAdapter
import com.rollingbits.mobile_test.model.UserDataModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// https://medium.com/@froger_mcs/inject-everything-viewholder-and-dagger-2-e1551a76a908
// https://medium.com/@hinchman_amanda/working-with-recyclerview-in-android-kotlin-84a62aef94ec
class MainView : AppCompatActivity() {
    private var offlineMode = false
    private var userData: UserDataModel.UserHeader? = null
    private lateinit var jsonDirectory: File
    private lateinit var adapter: RecyclerAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    val RESTUrl = "https://reqres.in/api/users?per_page=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()
        checkInternetConnection()
        initializeJSONObject(readJSONFile("jsonData"))

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(userData!!.data)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        if(userData!!.data.isEmpty()){
            initializeJSONObject(readJSONFile("jsonData"))
        }
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
    }

    private fun initializeJSONObject(jsonString: String) {
        userData = Klaxon().parse<UserDataModel.UserHeader>(jsonString)
    }

    private fun writeJSONFile(data: String) {
        val file = File(jsonDirectory, "/jsonData.json")
        FileOutputStream(file).use {
            it.write(data.toByteArray())
        }
    }

    private fun readJSONFile(filename: String): String{
        val filenamePath = File(jsonDirectory.path, "/" + filename + ".json")
        return FileInputStream(filenamePath).bufferedReader().use { it.readText() }
    }

    fun parse(name: String): Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser.default().parse(inputStream)
        }
    }

}

