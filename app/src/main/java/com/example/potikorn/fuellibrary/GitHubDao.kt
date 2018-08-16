package com.example.potikorn.fuellibrary

import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Reader

data class GithubDetail(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("full_name") var fullName: String? = null
) {
    class ListDeserializera : ResponseDeserializable<List<GithubDetail>> {
        override fun deserialize(reader: Reader): List<GithubDetail> {
            val type = object : TypeToken<List<GithubDetail>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}

fun githubDeserializer(json: JSON) =
    ::GithubDetail.create.map(json at "id").apply(json at "name").apply(json at "fullName")
