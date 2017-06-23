package io.github.Bashorg

import android.content.Context
import android.support.v7.app.AlertDialog
import io.github.Bashorg.bashclient.R

class CustomDialog(context: Context) : AlertDialog(context, android.R.style.Theme_Material_Light_NoActionBar) {

    override fun show() {
        super.show()
        setContentView(R.layout.loading_screen_activity)
    }


}
