package com.rollingbits.mobile_test.controller

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.irozon.mural.extension.source
import com.rollingbits.mobile_test.R
import com.rollingbits.mobile_test.model.UserDataModel
import com.rollingbits.mobile_test.extensions.inflate
import com.rollingbits.mobile_test.view.DetailUserView
import kotlinx.android.synthetic.main.recyclerview_item.view.*

// https://github.com/KingIdee/kodein-sample/tree/master/app/src/main/java/com/example/kodeinsample
// https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class RecyclerAdapter(private val user: List<UserDataModel.UserData>):
    RecyclerView.Adapter<RecyclerAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        val inflatedView = p0.inflate(R.layout.recyclerview_item, false)
        return UserHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val userData = user[position]
        holder.bindUser(userData)
    }

    class UserHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view: View = v
        private var userData: UserDataModel.UserData? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val showDetailUserIntent = Intent(context,DetailUserView::class.java)

            context.startActivity(showDetailUserIntent)
        }

        fun bindUser(userData: UserDataModel.UserData) {
            this.userData = userData

            view.firstNameTV.text = userData.first_name
            view.lastNameTV.text = userData.last_name
            view.emailTV.text = userData.email
            view.userAvatar.source = userData.avatar
        }

    }
}



