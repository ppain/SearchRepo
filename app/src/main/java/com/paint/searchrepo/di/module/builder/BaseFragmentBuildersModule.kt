package com.paint.searchrepo.di.module.builder

import com.paint.searchrepo.ui.base.auth.AuthFragment
import com.paint.searchrepo.ui.base.repo.RepoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class BaseFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun getAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    abstract fun getRepoFragment(): RepoFragment
}