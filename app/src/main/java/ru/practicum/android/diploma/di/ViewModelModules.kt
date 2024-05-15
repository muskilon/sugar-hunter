package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.favourite.FavouriteViewModel
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceViewModel
import ru.practicum.android.diploma.ui.filter.place.country.CountryViewModel
import ru.practicum.android.diploma.ui.filter.place.region.RegionViewModel
import ru.practicum.android.diploma.ui.filter.sphere.ChoiceSphereViewModel
import ru.practicum.android.diploma.ui.root.RootViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.team.TeamViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val viewModelModules = module {
    viewModel {
        FavouriteViewModel(favouriteDataBaseInteractor = get())
    }
    viewModel {
        CountryViewModel()
    }
    viewModel {
        RegionViewModel()
    }
    viewModel {
        ChoicePlaceViewModel()
    }
    viewModel {
        ChoiceSphereViewModel()
    }
    viewModel {
        FilterViewModel()
    }
    viewModel {
        RootViewModel()
    }
    viewModel {
        SearchViewModel(vacanciesInterActor = get())
    }
    viewModel {
        TeamViewModel()
    }
    viewModel {
            (vacancy: VacancyDetails) ->
        VacancyViewModel(vacancy = vacancy, get())
    }
}
