package com.example.potikorn.fuellibrary

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_empty_with_text.*

class SampleSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_with_text)

        FuelManager.instance.basePath = "https://api.github.com"

        "https://api.github.com/users/potikorn/repos".httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        ex.printStackTrace()
                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.i("GITHUB PROFILE", data)
                    }
                }
            }

//        "/users/potikorn/repos".httpGet()
//            .responseObject(GithubDetail.ListDeserializera()) { request, response, result ->
//                when (result) {
//                    is Result.Failure -> {
//                        val ex = result.getException()
//                        ex.printStackTrace()
//                    }
//                    is Result.Success -> {
//                        val data = result.get()
//                        Log.i("GITHUB RESPONSE OBJECT", data.toString())
//                        Log.i("GITHUB RESPONSE OBJECT", (data is List<GithubDetail>).toString())
//                        fetchText(data)
//                    }
//                }
//            }
        Fuel.request(GithubRouting.getRepoList())
            .responseObject(GithubDetail.ListDeserializera()) { request, response, result ->
                result.fold(success = { repoList ->
                    Log.i("GITHUB RESPONSE OBJECT", repoList.toString())
                    fetchText(repoList)
                }, failure = { error ->
                    error.printStackTrace()
                })
            }
    }

    private fun fetchText(data: List<GithubDetail>) {
        val spanText = StringBuilder()
        data.forEach {
            spanText.append("${it.name}\n")
        }
        tvText.text = spanText
    }
}