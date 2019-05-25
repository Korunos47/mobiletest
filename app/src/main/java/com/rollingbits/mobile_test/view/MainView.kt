package com.rollingbits.mobile_test.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.rollingbits.mobile_test.R
import com.rollingbits.mobile_test.controller.RecyclerAdapter
import com.rollingbits.mobile_test.model.UserDataModel
import kotlinx.android.synthetic.main.mainview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
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
        setContentView(R.layout.mainview)
        initialization()
        loadData()
    }

    private fun loadData() {
        if (checkInternetConnection()) {
            val file = File(jsonDirectory, "/jsonData.json")
            // Check if data is available from previous start
            if (file.exists())
                parseJsonToObject(readJSONFile("jsonData"))
            else
            // Get new JSON Data
                getJSON()
        } else {
            // offline -> read available data
            readExistingData()
        }
    }

    private fun initialization() {
        jsonDirectory = File(filesDir, "JSONData")
        if (!jsonDirectory.exists())
            jsonDirectory.mkdirs()

        linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
    }

    private fun checkInternetConnection(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun readExistingData() {
        if (jsonDirectory.exists()) {
            val file = File(jsonDirectory, "/jsonData.json")
            if (file.exists())
                parseJsonToObject(readJSONFile("jsonData"))
            else
                Toast.makeText(this, "Keine Daten zum Lesen vorhanden", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getJSON() {
        RESTUrl.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Toast.makeText(this, "Fehler beim Abruf", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    writeJSONFile(result.value)
                    parseJsonToObject(readJSONFile("jsonData"))
                }
            }
        }
    }

    private fun parseJsonToObject(jsonString: String) {
        userData = Klaxon().parse<UserDataModel.UserHeader>(jsonString)

        adapter = RecyclerAdapter(userData!!.data)
        recyclerView.adapter = adapter
    }

    private fun writeJSONFile(data: String) {
        val file = File(jsonDirectory, "/jsonData.json")
        FileOutputStream(file).use {
            it.write(data.toByteArray())
        }
    }

    private fun readJSONFile(filename: String): String {
        val filenamePath = File(jsonDirectory.path, "/" + filename + ".json")
        return FileInputStream(filenamePath).bufferedReader().use { it.readText() }
    }
}

