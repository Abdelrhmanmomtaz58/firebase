package com.momtaz.tast128

data class User(
    val fname: String? = null,
    val lname: String? = null
) {
    override fun toString(): String {
        return "User(fName=$fname, lName=$lname)"
    }
}

