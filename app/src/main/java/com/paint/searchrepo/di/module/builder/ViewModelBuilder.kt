package com.paint.searchrepo.di.module.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paint.searchrepo.di.scope.ViewModelKey
import com.paint.searchrepo.util.DaggerViewModelFactory
import com.paint.searchrepo.ui.base.auth.AuthViewModel
import com.paint.searchrepo.ui.base.repo.RepoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: RepoViewModel): ViewModel
}