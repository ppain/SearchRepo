package com.paint.searchrepo.ui.base.repo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paint.searchrepo.R
import com.paint.searchrepo.data.model.repo.Repo
import com.paint.searchrepo.databinding.FragmentRepoBinding
import com.paint.searchrepo.util.DebouncingQueryTextListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_repo.*
import javax.inject.Inject


class RepoFragment : DaggerFragment(), RepoAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var rvAdapter: RepoAdapter

    private val viewModel by lazy {
        ViewModelProviders.of(
            this@RepoFragment,
            viewModelFactory
        )[RepoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRepoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repo, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initToolbar()

        initObserve()
    }

    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.menu_toolbar)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        if (viewModel.checkAuth()) {
            toolbar.menu.findItem(R.id.actionAuth).title = getString(R.string.logOut)
        }

//        setup searchView
        val item: MenuItem = toolbar.menu.findItem(R.id.actionSearch)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                this@RepoFragment
            ) { newText ->
                newText?.let {
                    //                    not empty and not repeat
                    if (it.isNotEmpty() && it != viewModel.query) {
                        viewModel.newRepoRequest(it)
                    }
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionAuth -> {
                viewModel.actionAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        val llm = LinearLayoutManager(activity)
        rvRepo.layoutManager = llm
        rvRepo.setHasFixedSize(true)
        rvRepo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = llm.childCount
                    val pastVisibleItem = llm.findLastVisibleItemPosition()
                    val total = rvAdapter.itemCount
                    viewModel.checkNeedGetRepo(visibleItemCount, pastVisibleItem, total)
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initObserve() {
        viewModel.repoList().observe(
            this@RepoFragment,
            Observer<List<Repo>> {
                it?.let {
                    if (::rvAdapter.isInitialized) {
                        rvAdapter.setData(it)
                    } else {
                        rvAdapter = RepoAdapter(it, this@RepoFragment)
                        rvRepo.adapter = rvAdapter
                    }
                }
            })

        viewModel.navigateToAuth.observe(this@RepoFragment, Observer {
            if (it) {
                viewModel.navigateToAuthHandled()
                startAuthFragment()
            }
        })

        viewModel.message.observe(this@RepoFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    override fun onItemClick(position: Int) {
        showMessage("Number ${position + 1}")
//        do something
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun startAuthFragment() {
        findNavController().navigate(R.id.action_repoFragment_to_authFragment)
    }
}