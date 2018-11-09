package com.adjust.sdk.plugin;

import android.net.Uri;

import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.AdjustFactory;
import com.adjust.sdk.ILogger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by widerplanet on 21/12/2017.
 */
public class AdjustWiderPlanet {
    private static int MAX_ITEMS_ON_LISTING = 10;
    private static ILogger logger = AdjustFactory.getLogger();
    
    private static String clientIdInternal;
    
    private static String customerIdInternal;
    private static String hashedUIDInternal;
    private static String hashedEmailInternal;
    
    private static String checkInDateInternal;
    private static String checkOutDateInternal;

    public static void injectViewLoginIntoEvent(AdjustEvent event, String customerId) {
        customerIdInternal = customerId;
        injectOptionalParams(event);
    }

    public static void injectViewProductIntoEvent(AdjustEvent event, String productId) {
        event.addPartnerParameter("items", productId);
        injectOptionalParams(event);
    }

    public static void injectCartIntoEvent(AdjustEvent event, List<WiderPlanetProduct> products) {
        String jsonProducts = createWiderPlanetVBFromProducts(products);
        event.addPartnerParameter("items", jsonProducts);
        injectOptionalParams(event);
    }

    public static void injectTransactionConfirmedIntoEvent(AdjustEvent event, List<WiderPlanetProduct> products, String transactionId) {
        String jsonProducts = createWiderPlanetVBFromProducts(products);
        event.addPartnerParameter("order_id", transactionId);
        event.addPartnerParameter("items", jsonProducts);
        injectOptionalParams(event);
    }

    public static void injectHashedEmailIntoWiderPlanetEvents(String hashedEmail) {
        hashedEmailInternal = hashedEmail;
    }

    public static void injectHashedUIDIntoWiderPlanetEvents(String hashedUID) {
        hashedUIDInternal = hashedUID;
    }

    public static void injectViewSearchDatesIntoWiderPlanetEvents(String checkInDate, String checkOutDate) {
        checkInDateInternal = checkInDate;
        checkOutDateInternal = checkOutDate;
    }

    public static void injectClientIdIntoWiderPlanetEvents(String partnerId) {
        clientIdInternal = partnerId;
    }

    public static void injectCustomerIdIntoWiderPlanetEvents(String customerId) {
        customerIdInternal = customerId;
    }

    public static void injectDeeplinkIntoEvent(AdjustEvent event, Uri url) {
        if (url == null) {
            return;
        }
        event.addPartnerParameter("loc", url.toString());
        injectOptionalParams(event);
    }

    private static void injectOptionalParams(AdjustEvent event) {
        injectClientId(event);
        injectHashedUID(event);
        injectHashedEmail(event);
        injectCustomerId(event);
    }

    private static void injectHashedUID(AdjustEvent event) {
        if (hashedUIDInternal == null || hashedUIDInternal.isEmpty()) {
            return;
        }
        event.addPartnerParameter("hcuid", hashedUIDInternal);
    }

    private static void injectHashedEmail(AdjustEvent event) {
        if (hashedEmailInternal == null || hashedEmailInternal.isEmpty()) {
            return;
        }
        event.addPartnerParameter("hceid", hashedEmailInternal);
    }

    private static void injectClientId(AdjustEvent event) {
        if (clientIdInternal == null || clientIdInternal.isEmpty()) {
            return;
        }
        event.addPartnerParameter("ti", clientIdInternal);
    }

    private static void injectCustomerId(AdjustEvent event) {
        if (customerIdInternal == null || customerIdInternal.isEmpty()) {
            return;
        }
        event.addPartnerParameter("cuid", customerIdInternal);
    }

    private static String createWiderPlanetPVFromProducts(List<String> productIds) {
        if (productIds == null) {
            logger.warn("WiderPlanet View Listing product ids list is null. It will sent as empty.");
            productIds = new ArrayList<String>();
        }
        int productIdsSize = productIds.size();
        
        if (productIdsSize > MAX_ITEMS_ON_LISTING) {
            logger.warn("WiderPlanet View Listing should only have at most %d product ids. The rest will be discarded.",MAX_ITEMS_ON_LISTING);
        }
        StringBuffer WPVLValue = new StringBuffer("[");

        for (int i = 0; i < productIdsSize; ) {
            String productID = productIds.get(i);
            String productString = String.format(Locale.US, "{\"event_item_id\":\"%s\"", productID);
            productString.append("}");
            WPVLValue.append(productString);
            i++;
            if (i == productIdsSize || i >= MAX_ITEMS_ON_LISTING) {
                break;
            }
            WPVLValue.append(",");
        }
        WPVLValue.append("]");
        String result = null;
        try {
            result = URLEncoder.encode(WPVLValue.toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error converting WP product ids (%s)", e.getMessage());
        }
        return result;
    }

    private static String createWiderPlanetVBFromProducts(List<WiderPlanetProduct> products) {
        if (products == null) {
            logger.warn("WiderPlanet Event product list is empty. It will sent as empty.");
            products = new ArrayList<WiderPlanetProduct>();
        }
        StringBuffer WPVBValue = new StringBuffer("[");
        int productsSize = products.size();
        for (int i = 0; i < productsSize; ) {
            WiderPlanetProduct WPProduct = products.get(i);
            String productString = String.format(Locale.US, "{\"event_item_id\":\"%s\",\"unit_price\":%f,\"quantity\":%d}",
                    WPProduct.product_id,
                    WPProduct.price,
                    WPProduct.quantity);
            WPVBValue.append(productString);
            i++;

            if (i == productsSize) {
                break;
            }
            WPVBValue.append(",");
        }
        WPVBValue.append("]");
        String result = null;
        try {
            result = URLEncoder.encode(WPVBValue.toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error converting WP products (%s)", e.getMessage());
        }
        return result;
    }
}