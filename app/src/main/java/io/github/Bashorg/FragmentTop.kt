package io.github.Bashorg

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import io.github.Bashorg.bashclient.R
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*

class FragmentTop : android.support.v4.app.Fragment() {

    private var mQuotesAdapter: ArrayAdapter<String>? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        update("top")
    }

    private fun update(s: String) {
        val quotesLoader = QuotesLoader()
        quotesLoader.execute(s)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mQuotesAdapter = ArrayAdapter(
                activity,
                R.layout.list_quotes,
                R.id.list_item_quote_textview,
                ArrayList<String>())

        val rootView = inflater!!.inflate(R.layout.quotes_listview, container, false)

        mSwipeRefreshLayout = rootView.findViewById(R.id.quotes_main) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setColorSchemeResources(R.color.colorAccent)
        mSwipeRefreshLayout!!.setOnRefreshListener { update("top") }

        val listView = rootView.findViewById(R.id.listview_quotes) as ListView
        listView.adapter = mQuotesAdapter

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, pos, _ ->
            val selectedText = listView.getItemAtPosition(pos) as String
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, selectedText)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, resources.getText(R.string.send_to)))
            true
        }
        return rootView
    }

    private inner class QuotesLoader : AsyncTask<String, Void, ArrayList<String>>() {

        internal var dialog = CustomDialog(activity)
        internal var arrayOfQuotes = ArrayList<String>()
        override fun onPreExecute() {
            super.onPreExecute()
            if (mQuotesAdapter == null) {
                dialog.show()
            }
        }

        override fun doInBackground(vararg params: String): ArrayList<String>? {
            if (params.isEmpty()) {
                return null
            }
            val quotes: Elements
            var quoteString: String

            val BASE_URL = "http://www.bash.org/?"
            val builtUrl = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("", params[0])
                    .build()
            val url = builtUrl.toString().replace("=", "")

            try {
                val document = Jsoup.connect(url).get()
                quotes = document.select("p.qt")
                for (element in quotes) {
                    quoteString = element
                            .toString()
                            .replace("<p class=\"qt\">", "")
                            .replace("<br>", "\n")
                            .replace("</p>", "")
                            .replace("&lt;", "<")
                            .replace("&gt;", ">")
                            .replace("&nbsp;", "\t")
                    arrayOfQuotes.add(quoteString)
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            return arrayOfQuotes
        }

        override fun onPostExecute(result: ArrayList<String>?) {
            if (result != null) {
                mQuotesAdapter!!.clear()
                for (quoteString in result) {
                    mQuotesAdapter!!.add(quoteString)
                }
            }
            dialog.dismiss()
            mSwipeRefreshLayout!!.isRefreshing = false
        }
    }
}