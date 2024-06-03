package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favourite.FavouriteViewModel
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.filter.industry.ChooseIndustryViewModel
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceViewModel
import ru.practicum.android.diploma.ui.filter.place.country.CountryViewModel
import ru.practicum.android.diploma.ui.filter.place.region.RegionViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val viewModelModules = module {
    viewModel {
        FavouriteViewModel(favouriteDataBaseInteractor = get())
    }
    viewModel {
        CountryViewModel(get())
    }
    viewModel {
        RegionViewModel(get())
    }
    viewModel {
        ChoicePlaceViewModel()
    }
    viewModel {
        ChooseIndustryViewModel(industryInteractor = get())
    }
    viewModel {
        FilterViewModel(filtersInterActor = get())
    }
    viewModel {
        SearchViewModel(vacanciesInterActor = get(), filtersInterActor = get())
    }
    viewModel { (id: String) ->
        VacancyViewModel(vacancyId = id, favouriteDataBaseInteractor = get(), vacanciesInterActor = get())
    }
}
