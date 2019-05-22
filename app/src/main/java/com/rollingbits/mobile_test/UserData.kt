package com.rollingbits.mobile_test

class UserData {
    data class UserHeader(
        val page: Int,
        val per_page: Int,
        val total: Int,
        val total_pages: Int,
        val data: List<UserData>
    ){

    data class UserData(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val avatar: String)
    }
}