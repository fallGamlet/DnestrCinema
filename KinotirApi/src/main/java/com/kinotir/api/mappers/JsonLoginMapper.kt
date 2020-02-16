package com.kinotir.api.mappers

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

internal class JsonLoginMapper : Mapper<String?, Boolean> {

    override fun map(src: String?): Boolean {
        val loginResult = try {
            Gson().fromJson(src, LoginResult::class.java)
        } catch (ignored: Exception) {
            LoginResult()
        }
        return loginResult.success
    }

    private inner class LoginResult {
        @SerializedName("success")
        var success = false
        @SerializedName("error")
        var error: String? = null
    }
}
