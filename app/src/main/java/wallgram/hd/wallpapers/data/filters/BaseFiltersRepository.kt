package wallgram.hd.wallpapers.data.filters

import wallgram.hd.wallpapers.data.colors.ColorMapper
import wallgram.hd.wallpapers.data.colors.ColorsDataSource
import wallgram.hd.wallpapers.domain.filters.CategoryDomain
import wallgram.hd.wallpapers.domain.filters.CategoriesDomain
import wallgram.hd.wallpapers.domain.filters.CategoriesRepository
import javax.inject.Inject

class BaseFiltersRepository @Inject constructor(
    private val cloudDataSource: FiltersCloudDataSource,
    private val colorsDataSource: ColorsDataSource,
    private val mapper: FiltersCloud.Mapper<CategoryDomain>,
    private val colorMapper: ColorMapper
) : CategoriesRepository {

    override suspend fun filters(): CategoriesDomain {
        val categories = cloudDataSource.categories()
        val colors = colorsDataSource.colors()
        return CategoriesDomain.Base(
            categories.map { it.map(mapper) },
            colors.map { it.map(colorMapper) })
    }

    override fun save(id: Int, requestId: String) {

    }
}