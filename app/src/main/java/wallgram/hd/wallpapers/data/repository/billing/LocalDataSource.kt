package wallgram.hd.wallpapers.data.repository.billing

import wallgram.hd.wallpapers.data.local.Database
import wallgram.hd.wallpapers.model.SubscriptionStatus
import java.util.concurrent.Executors
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val appDatabase: Database
) {

    /**
     * Get the list of subscriptions from the localDataSource and get notified when the data changes.
     */
    val subscriptions = appDatabase.subscriptionStatusDao().getAll()

    fun updateSubscriptions(subscriptions: List<SubscriptionStatus>) {
        Executors.newSingleThreadExecutor().execute {
            appDatabase.runInTransaction {
                // Delete existing subscriptions.
                appDatabase.subscriptionStatusDao().deleteAll()
                // Put new subscriptions data into localDataSource.
                appDatabase.subscriptionStatusDao().insertAll(subscriptions)
            }
        }
    }

    /**
     * Delete local user data when the user signs out.
     */
    fun deleteLocalUserData() = updateSubscriptions(listOf())

}