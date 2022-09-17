package wallgram.hd.wallpapers.domain.filters

import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import javax.inject.Inject

interface CategoriesInteractor {

    suspend fun filters(
        atFinish: () -> Unit,
        successful: (FiltersUi) -> Unit
    )

    class Base @Inject constructor(
        private val mapper: CategoriesDomain.Mapper<FiltersUi>,
        private val repository: CategoriesRepository,
        dispatchers: Dispatchers,
        handleError: HandleError
    ) : Interactor.Abstract(dispatchers, handleError), CategoriesInteractor {

        override suspend fun filters(
            atFinish: () -> Unit,
            successful: (FiltersUi) -> Unit
        ) = handle(successful, atFinish) {
            val data = repository.filters()
            return@handle data.map(mapper)
        }
    }
}