package com.opiumfive.livetypingdemo.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.opiumfive.livetypingdemo.R
import com.opiumfive.livetypingdemo.util.RecyclerLineDecorator
import com.opiumfive.livetypingdemo.util.RecyclerScrollListener
import com.opiumfive.livetypingdemo.data.Meal
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class CatalogFragment : Fragment() {

    val viewModel: MealsViewModel by viewModel()

    val catalogObs: Observer<List<Meal>> by lazy { Observer<List<Meal>> { addProducts(it) } }

    val adapter = MealsAdapter()

    private fun initUI() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(RecyclerLineDecorator(requireContext()))
        recycler.addOnScrollListener(RecyclerScrollListener {
            downProgress.visibility = View.VISIBLE
            viewModel.getNextCategory()
        })

        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            adapter.clear()
            progress.visibility = View.VISIBLE
            viewModel.getCats(true)
        }
    }

    private fun addProducts(list: List<Meal>?) {
        progress.visibility = View.GONE
        downProgress.visibility = View.GONE
        adapter.addList(list ?: emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.catalogData.observe(this, catalogObs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        progress.visibility = View.VISIBLE

        if (viewModel.loaded.isEmpty()) {
            viewModel.getCats()
        } else {
            adapter.clear()
            viewModel.getCurrentList()
        }
    }
}