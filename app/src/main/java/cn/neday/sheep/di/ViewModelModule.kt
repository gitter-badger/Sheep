package cn.neday.sheep.di

import cn.neday.sheep.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { CreditHistoryViewModel(get()) }

    viewModel { NineListViewModel(get()) }

    viewModel { IndexViewModel(get(), get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { MainViewModel() }

    viewModel { RankingListViewModel(get()) }

    viewModel { SearchResultViewModel(get()) }

    viewModel { SearchViewModel(get()) }
}