package com.rollingbits.mobile_test.controller

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rollingbits.mobile_test.R
import com.rollingbits.mobile_test.model.UserDataModel
import com.rollingbits.mobile_test.extensions.inflate
import com.rollingbits.mobile_test.view.DetailUserView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerAdapter(private val user: List<UserDataModel.UserHeader.UserData>):
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
        private var userData: UserDataModel.UserHeader.UserData? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val showDetailUserIntent = Intent(context,DetailUserView::class.java)
            showDetailUserIntent.putExtra("first_name", userData!!.first_name)
            showDetailUserIntent.putExtra("last_name", userData?.last_name)
            showDetailUserIntent.putExtra("avatar", userData?.avatar)
            showDetailUserIntent.putExtra("email", userData?.email)
            context.startActivity(showDetailUserIntent)
        }

        fun bindUser(userData: UserDataModel.UserHeader.UserData) {
            this.userData = userData

            view.firstNameTV.text = userData.first_name
            view.lastNameTV.text = userData.last_name
            view.emailTV.text = userData.email

            if(checkInternetConnection(itemView.context))
                Picasso.get().load(userData.avatar).into(view.userAvatar)
        }

        private fun checkInternetConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}



