package com.paint.searchrepo.di.module.builder

import com.paint.searchrepo.ui.base.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [ViewModelBuilder::class]
)
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [BaseFragmentBuildersModule::class])
    internal abstract fun contributeBaseActivity(): BaseActivity
}