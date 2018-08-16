package com.example.potikorn.fuellibrary.coroutine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.potikorn.fuellibrary.GithubDetail
import com.example.potikorn.fuellibrary.GithubRouting
import com.example.potikorn.fuellibrary.R
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_empty_with_text.*
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class HttpCoroutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_with_text)

        launch(UI) {
            val result =
                withContext(DefaultDispatcher) {
                    Fuel.request(GithubRouting.getRepoList())
                        .responseObject(GithubDetail.ListDeserializera())
                }
            result.third.fold(success = { fetchText(it) },
                failure = {
                    Toast.makeText(
                        this@HttpCoroutine,
                        it.errorData.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
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