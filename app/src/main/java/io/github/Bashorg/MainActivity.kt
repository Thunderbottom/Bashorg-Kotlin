package io.github.Bashorg

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.github.Bashorg.bashclient.R

class MainActivity : AppCompatActivity() {

    internal var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.pager) as ViewPager
        val fragmentManager = supportFragmentManager
        mViewPager!!.adapter = io.github.Bashorg.MyAdapter(fragmentManager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_info) {
            startActivity(Intent(this, InfoActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}