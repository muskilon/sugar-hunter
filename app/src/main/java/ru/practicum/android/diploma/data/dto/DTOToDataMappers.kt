package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.domain.models.AreaDictionary
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.LogoUrls
import ru.practicum.android.diploma.domain.models.Phones
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class DTOToDataMappers {
    fun mapDetailsResponseToVacancyDetails(vacancy: DetailsResponse) =
        VacancyDetails(
            id = vacancy.id,
            title = vacancy.name,
            city = vacancy.area.name,
            employer = vacancy.employer.name,
            salary = mapSalaryDTOToSalary(vacancy.salary),
            experience = Experience(
                id = vacancy.experience?.id,
                name = vacancy.experience?.name
            ),
            employment = Employment(
                id = vacancy.employment?.id,
                name = vacancy.employment?.name
            ),
            schedule = Schedule(
                id = vacancy.schedule?.id,
                name = vacancy.schedule?.name
            ),
            description = vacancy.description,
            keySkills = skillsMapper(vacancy.keySkills),
            contacts = Contacts(
                email = vacancy.contacts?.email,
                name = vacancy.contacts?.name,
                phones = vacancy.contacts?.phones?.map {
                    val phones = Phones(
                        city = it.city,
                        country = it.country,
                        comment = it.comment,
                        formatted = it.formatted,
                        number = it.number
                    )
                    phones
                }
            ),
            logoUrls = mapLogoUrlsDTOToLogoUrls(vacancy.employer.logoUrls),
            url = vacancy.url
        )
    fun mapSearchResponseToVacanciesResponse(data: SearchResponseDTO) =
        VacanciesResponse(
            page = data.page,
            pages = data.pages,
            found = data.found,
            items = data.items.map {
                val vacancy = Vacancy(
                    id = it.id,
                    title = it.name,
                    city = it.area.name,
                    employer = it.employer.name,
                    logos = mapLogoUrlsDTOToLogoUrls(it.employer.logoUrls),
                    salary = mapSalaryDTOToSalary(it.salary)
                )
                vacancy
            }
        )
    fun industryResponseToIndustries(data: IndustryResponse) =
        data.container.map {
            val industries = Industries(
                id = it.id,
                name = it.name,
                industries = it.industries.map { sub ->
                    val subIndustry = Industry(
                        id = sub.id,
                        name = sub.name
                    )
                    subIndustry
                }
            )
            industries
        }
    fun areasDTOToAreaDictionary(data: AreasDTO) =
        data.container.map{
            val areas = AreaDictionary(
                id = it.id,
                parentId = it.parentId,
                name = it.name,
                areas = it.areas.map { sub ->
                    val subArea = AreaDictionary(
                        id = it.id,
                        parentId = it.parentId,
                        name = it.name,
                        areas = it.areas
                    )
                }
            )
        }
    private fun mapSalaryDTOToSalary(salary: SalaryDTO?) =
        Salary(
            from = salary?.from,
            to = salary?.to,
            currency = salary?.currency,
            gross = salary?.gross
        )
    private fun mapLogoUrlsDTOToLogoUrls(logoUrls: LogoUrlsDTO?) =
        LogoUrls(
            logo90 = logoUrls?.logo90,
            logo240 = logoUrls?.logo240
        )
    private fun skillsMapper(list: List<KeySkillsDTO>?): List<String>? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return list.mapNotNull { it.name }
    }
}
