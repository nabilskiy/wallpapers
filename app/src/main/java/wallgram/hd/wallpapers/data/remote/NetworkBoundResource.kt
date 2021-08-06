package wallgram.hd.wallpapers.data.remote

import kotlinx.coroutines.flow.*
import wallgram.hd.wallpapers.data.Resource

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
    ) = flow<Resource<ResultType>> {
        emit(Resource.Loading(null))
        val data = query().first()

        val flow = if (shouldFetch(data)) {
            emit(Resource.Loading(data))

            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
//            try {
//
//            } catch (throwable: Throwable) {
//
//                onFetchFailed(throwable)
//                query().map { Resource.DataError(0) }
//            }
        } else {
            query().map { Resource.Success(it) }
        }

        emitAll(flow)
    }