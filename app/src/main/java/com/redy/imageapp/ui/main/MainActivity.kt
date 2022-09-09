package com.redy.imageapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redy.imageapp.MyApplication
import com.redy.imageapp.R
import com.redy.imageapp.network.ApiResponse
import com.redy.imageapp.ui.image.ImageActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val dispatchersIC = MyApplication.dispatchersIC
    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModel.create(
            imageRepository = MyApplication.imageRepository,
            dispatcher = dispatchersIC
        )
    })

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest { apiResponse ->
                    withContext(dispatchersIC.ui) {
                        when(apiResponse) {
                            is ApiResponse.Loading -> renderProgressBar(visible = true)
                            is ApiResponse.Fail -> showError()
                            is ApiResponse.Success -> renderItemList(apiResponse.result)
                        }
                    }
                }
            }
        }

        viewModel.loadData()
    }

    private fun renderItemList(state: List<ItemRow>) {
        //toolbar.title = state.toolbarTitle
        renderProgressBar(false)
        adapter.update(state)
    }

    private fun renderProgressBar(visible: Boolean) {
        when(visible) {
            true -> progressBar.visibility = View.VISIBLE
            else -> progressBar.visibility = View.GONE
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ops Error!!", Toast.LENGTH_SHORT).show()
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter { itemRow ->
            startActivity(itemRow)
        }
        recyclerView.adapter = adapter

        progressBar = findViewById(R.id.progressBar)
    }

    private fun startActivity(item: ItemRow) {
        val intent = ImageActivity.intent(this, item.image)
        startActivity(intent)
    }
}

