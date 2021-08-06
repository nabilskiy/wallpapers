package wallgram.hd.wallpapers.util;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import java.util.List;

public class BillingHelper {

    public static boolean areSubscriptionsSupported(BillingClient mBillingClient) {
        BillingResult billingResult = mBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        return billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    public static List<Purchase> queryPurchases(BillingClient mBillingClient) {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
        return purchasesResult.getPurchasesList();
    }

    public static boolean handlePurchase(Purchase purchase, BillingClient mBillingClient) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                });
            }
            return true;
        }
        return false;
    }

}
