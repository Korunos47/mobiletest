package com.rollingbits.mobile_test

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

// https://github.com/KingIdee/kodein-sample/tree/master/app/src/main/java/com/example/kodeinsample
// https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class RecyclerAdapter(private val user: List<UserData>):
    RecyclerView.Adapter<RecyclerAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        val inflatedView = p0.inflate(R.layout.recyclerview_item, false)
        return UserHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(p0: UserHolder, p1: Int) {

    }

    class UserHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view: View = v
        private var userData: UserData.UserHeader.UserData? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

    }
}

