package com.paint.searchrepo.ui.base.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.paint.searchrepo.R
import com.paint.searchrepo.databinding.FragmentAuthBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AuthFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this@AuthFragment, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        If exist last success auth -> skipping
        viewModel.checkLastAuth()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initObserve() {
        viewModel.navigateToRepo.observe(this@AuthFragment, Observer {
            if (it) {
                viewModel.navigateToRepoHandled()
                startRepoFragment()
            }
        })

        viewModel.message.observe(this@AuthFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun startRepoFragment() {
        findNavController().navigate(R.id.action_authFragment_to_repoFragment)
    }
}