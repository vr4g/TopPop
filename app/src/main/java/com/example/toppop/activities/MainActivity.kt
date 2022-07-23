package com.example.toppop.activities

import MyAdapter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.toppop.R
import com.example.toppop.RetrofitHelper
import com.example.toppop.api.DeezerApi
import okhttp3.internal.wait

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = LinearLayoutManager(this)
        getChart(1)
    }

    private fun getChart(selectedMenuItem: Int) {

        val errorMessage = findViewById<TextView>(R.id.noConnectionInfo)
        val errorMessageInfo = findViewById<TextView>(R.id.errorInfo)
        val restartApp = findViewById<Button>(R.id.restartAppButton)

        errorMessage.visibility = TextView.GONE
        errorMessageInfo.visibility = TextView.GONE
        restartApp.visibility = Button.GONE

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected) {

                var retrofit = RetrofitHelper.getInstance()
                var apiInterface = retrofit.create(DeezerApi::class.java)

                lifecycleScope.launchWhenCreated {
                    val response = apiInterface.getTopTen()

                    try {
                        if (response.isSuccessful) {
                            var adapterData = response.body()?.tracks?.data!!

                            val refresh = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
                            refresh.visibility = SwipeRefreshLayout.VISIBLE
                            refresh.setOnRefreshListener {
                                refresh.isRefreshing = false
                                getChart(selectedMenuItem)
                            }
                            when (selectedMenuItem) {
                                1 -> adapterData = response.body()?.tracks?.data!!
                                2 -> adapterData =
                                    response.body()?.tracks?.data?.sortedWith(compareBy({ it.duration }))!!
                                3 -> adapterData =
                                    response.body()?.tracks?.data?.sortedWith(compareBy({ it.duration }))
                                        ?.reversed()!!
                            }

                            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                                myAdapter = MyAdapter(adapterData)
                                layoutManager = manager
                                adapter = myAdapter
                            }
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
            }
        } else {
            restartApp.setOnClickListener {
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0);
            }
            restartApp.visibility = Button.VISIBLE
            errorMessage.visibility = TextView.VISIBLE
            errorMessageInfo.text = getString(R.string.error_info)
            errorMessageInfo.visibility = TextView.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.normal) {
            getChart(1)
            return true
        }
        if (item.itemId == R.id.asc_sort) {
            getChart(2)
            return true
        }
        if (item.itemId == R.id.desc_sort) {
            getChart(3)
            return true
        }
        return false
    }
}

