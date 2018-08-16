package com.example.potikorn.fuellibrary

import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.util.FuelRouting

sealed class GithubRouting : FuelRouting {

    override val basePath: String
        get() = "https://api.github.com" // can use constant variable or build config

    class getRepoList() : GithubRouting() {
        override val body: String?
            get() = null
        override val headers: Map<String, String>?
            get() = null
        override val method: Method
            get() = Method.GET
        override val params: List<Pair<String, Any?>>?
            get() = null
        override val path: String
            get() = "/users/potikorn/repos"
    }
}