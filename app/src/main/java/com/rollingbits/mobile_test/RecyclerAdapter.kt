package com.rollingbits.mobile_test

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

// https://github.com/KingIdee/kodein-sample/tree/master/app/src/main/java/com/example/kodeinsample
// https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class RecyclerAdapter: RecyclerView.Adapter<RecyclerView.UserHolder>() {
    private lateinit var userData: UserData

    fun RecyclerAdapter(userData: UserData){

    }
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}