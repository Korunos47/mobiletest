package com.rollingbits.mobile_test

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// https://medium.com/@froger_mcs/inject-everything-viewholder-and-dagger-2-e1551a76a908
// https://medium.com/@hinchman_amanda/working-with-recyclerview-in-android-kotlin-84a62aef94ec
class MainActivity : AppCompatActivity(),KodeinAware {
    private var offlineMode = false
    private var userData: UserData.UserHeader? = null
    private lateinit var jsonDirectory: File

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

    }

    private fun initialization() {
        jsonDirectory = File(filesDir, "JSONData")
    }

    override val kodein: Kodein
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

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
        userData = Klaxon().parse<UserData.UserHeader>(jsonString)
    }

    private fun writeJSONFile(data: String) {
        val file = File(jsonDirectory, "/jsonData.json")
        FileOutputStream(file).use {
            it.write(data.toByteArray())
        }
    }

    private fun readJSONFile(filename: String): String{
        val filename = File(jsonDirectory.path, "/" + filename + ".json")
        return FileInputStream(filename).bufferedReader().use { it.readText() }
    }

    fun parse(name: String): Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser.default().parse(inputStream)
        }
    }

}

