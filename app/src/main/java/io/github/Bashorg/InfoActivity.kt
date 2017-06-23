package io.github.Bashorg

import android.app.Activity
import android.os.Bundle
import io.github.Bashorg.bashclient.R

class InfoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_layout)
    }
}
