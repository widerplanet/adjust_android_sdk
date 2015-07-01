package com.adjust.sdk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdjustCordova extends CordovaPlugin implements OnAttributionChangedListener {
    private static final String BOOL_TRUE                           = "true";
    private static final String SDK_PREFIX                          = "cordova4.0.0";

    private static final String KEY_APP_TOKEN                       = "appToken";
    private static final String KEY_ENVIRONMENT                     = "environment";
    private static final String KEY_LOG_LEVEL                       = "logLevel";
    private static final String KEY_PROCESS_NAME                    = "processName";
    private static final String KEY_DEFAULT_TRACKER                 = "defaultTracker";
    private static final String KEY_EVENT_BUFFERING_ENABLED         = "eventBufferingEnabled";
    private static final String KEY_EVENT_TOKEN                     = "eventToken";
    private static final String KEY_REVENUE                         = "revenue";
    private static final String KEY_CURRENCY                        = "currency";
    private static final String KEY_CALLBACK_PARAMETERS             = "callbackParameters";
    private static final String KEY_PARTNER_PARAMETERS              = "partnerParameters";

    private static final String COMMAND_CREATE                      = "create";
    private static final String COMMAND_SET_ATTRIBUTION_CALLBACK    = "setAttributionCallback";
    private static final String COMMAND_TRACK_EVENT                 = "trackEvent";
    private static final String COMMAND_SET_OFFLINE_MODE            = "setOfflineMode";
    private static final String COMMAND_ON_RESUME                   = "onResume";
    private static final String COMMAND_ON_PAUSE                    = "onPause";
    private static final String COMMAND_IS_ENABLED                  = "isEnabled";
    private static final String COMMAND_SET_ENABLED                 = "setEnabled";

    private static String callbackId;
    private static CordovaWebView cordovaWebView;
    private static boolean isAttributionCallbackSet = false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_CREATE)) {
            JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, String> parameters = jsonObjectToMap(jsonParameters);

            String appToken = parameters.get(KEY_APP_TOKEN);
            String environment = parameters.get(KEY_ENVIRONMENT);
            String defaultTracker = parameters.get(KEY_DEFAULT_TRACKER);
            String processName = parameters.get(KEY_PROCESS_NAME);

            String logLevel = parameters.get(KEY_LOG_LEVEL);
            String eventBufferingEnabled = parameters.get(KEY_EVENT_BUFFERING_ENABLED);

            AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity(), appToken, environment);

            if (adjustConfig.isValid()) {
                // Log level
                if (isFieldValid(logLevel)) {
                    if (logLevel.equals("VERBOSE")) {
                        adjustConfig.setLogLevel(LogLevel.VERBOSE);
                    } else if (logLevel.equals("DEBUG")) {
                        adjustConfig.setLogLevel(LogLevel.DEBUG);
                    } else if (logLevel.equals("INFO")) {
                        adjustConfig.setLogLevel(LogLevel.INFO);
                    } else if (logLevel.equals("WARN")) {
                        adjustConfig.setLogLevel(LogLevel.WARN);
                    } else if (logLevel.equals("ERROR")) {
                        adjustConfig.setLogLevel(LogLevel.ERROR);
                    } else if (logLevel.equals("ASSERT")) {
                        adjustConfig.setLogLevel(LogLevel.ASSERT);
                    } else {
                        adjustConfig.setLogLevel(LogLevel.INFO);
                    }
                }

                // Event buffering
                if (isFieldValid(eventBufferingEnabled)) {
                    if (eventBufferingEnabled.equals(BOOL_TRUE)) {
                        adjustConfig.setEventBufferingEnabled(true);
                    } else {
                        adjustConfig.setEventBufferingEnabled(false);
                    }
                }

                // SDK Prefix
                // No matter what is maybe set, we're setting it in here.
                adjustConfig.setSdkPrefix(SDK_PREFIX);

                // Main process name
                if (isFieldValid(processName)) {
                    adjustConfig.setProcessName(processName);
                }

                // Default tracker
                if (isFieldValid(defaultTracker)) {
                    adjustConfig.setDefaultTracker(defaultTracker);
                }

                // Attribution callback
                if (isAttributionCallbackSet) {
                    adjustConfig.setOnAttributionChangedListener(this);
                }

                Adjust.onCreate(adjustConfig);

                // Needed because Cordova doesn't launch 'resume' event on app start.
                // It initializes it only when app comes back from the background.
                Adjust.onResume();
            }

            return true;
        } else if (action.equals(COMMAND_SET_ATTRIBUTION_CALLBACK)) {
            AdjustCordova.callbackId = callbackContext.getCallbackId();
            AdjustCordova.cordovaWebView = this.webView;

            isAttributionCallbackSet = true;

            return true;
        } else if (action.equals(COMMAND_TRACK_EVENT)) {
            JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, String> parameters = jsonObjectToMap(jsonParameters);

            String eventToken = parameters.get(KEY_EVENT_TOKEN);
            String revenue = parameters.get(KEY_REVENUE);
            String currency = parameters.get(KEY_CURRENCY);

            JSONObject partnerParametersJson = new JSONObject(parameters.get(KEY_PARTNER_PARAMETERS));
            JSONObject callbackParametersJson = new JSONObject(parameters.get(KEY_CALLBACK_PARAMETERS));
            Map<String, String> partnerParameters = jsonObjectToMap(partnerParametersJson);
            Map<String, String> callbackParameters = jsonObjectToMap(callbackParametersJson);

            AdjustEvent adjustEvent = new AdjustEvent(eventToken);

            if (adjustEvent.isValid()) {
                if (isFieldValid(revenue)) {
                    try {
                        double revenueValue = Double.parseDouble(revenue);

                        adjustEvent.setRevenue(revenueValue, currency);
                    } catch (Exception e) {
                        ILogger logger = AdjustFactory.getLogger();
                        logger.error("Unable to parse revenue");
                    }
                }

                for (Map.Entry<String, String> parameter : callbackParameters.entrySet()) {
                    adjustEvent.addCallbackParameter(parameter.getKey(), parameter.getValue());
                }

                for (Map.Entry<String, String> parameter : partnerParameters.entrySet()) {
                    adjustEvent.addPartnerParameter(parameter.getKey(), parameter.getValue());
                }

                Adjust.trackEvent(adjustEvent);
            }

            return true;
        } else if (action.equals(COMMAND_SET_OFFLINE_MODE)) {
            Boolean enabled = args.getBoolean(0);
            Adjust.setOfflineMode(enabled);

            return true;
        } else if (action.equals(COMMAND_ON_PAUSE)) {
            Adjust.onPause();

            return true;
        } else if (action.equals(COMMAND_ON_RESUME)) {
            Adjust.onResume();

            return true;
        } else if (action.equals(COMMAND_SET_ENABLED)) {
            Boolean enabled = args.getBoolean(0);
            Adjust.setEnabled(enabled);

            return true;
        } else if (action.equals(COMMAND_IS_ENABLED)) {
            Boolean isEnabled = Adjust.isEnabled();
            PluginResult pluginResult = new PluginResult(Status.OK, isEnabled);
            callbackContext.sendPluginResult(pluginResult);

            return true;
        }

        String errorMessage = String.format("Invalid call (%s)", action);

        Logger logger = (Logger) AdjustFactory.getLogger();
        logger.error(errorMessage);

        return false;
    }

    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);

        CallbackContext callbackResponseData = new CallbackContext(AdjustCordova.callbackId, AdjustCordova.cordovaWebView);
        callbackResponseData.sendPluginResult(pluginResult);
    }

    boolean isFieldValid(String field) {
        if (field != null) {
            if (!field.equals("") && !field.equals("null")) {
                return true;
            }
        }

        return false;
    }

    private Map<String, String> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
        Map<String, String> map = new HashMap<String, String>(jsonObject.length());

        @SuppressWarnings("unchecked")
        Iterator<String> jsonObjectIterator = jsonObject.keys();

        while (jsonObjectIterator.hasNext()) {
            String key = jsonObjectIterator.next();
            map.put(key, jsonObject.getString(key));
        }

        return map;
    }

    private Map<String, String> getAttributionDictionary(AdjustAttribution attribution) {
        Map<String, String> attributionDataDic = new HashMap<String, String>();

        // Tracker token
        if (attribution.trackerToken != null) {
            attributionDataDic.put("trackerToken", attribution.trackerToken);
        } else {
            attributionDataDic.put("trackerToken", "");
        }

        // Tracker name
        if (attribution.trackerName != null) {
            attributionDataDic.put("trackerName", attribution.trackerName);
        } else {
            attributionDataDic.put("trackerName", "");
        }

        // Network
        if (attribution.network != null) {
            attributionDataDic.put("network", attribution.network);
        } else {
            attributionDataDic.put("network", "");
        }

        // Campaign
        if (attribution.campaign != null) {
            attributionDataDic.put("campaign", attribution.campaign);
        } else {
            attributionDataDic.put("campaign", "");
        }

        // Adgroup
        if (attribution.adgroup != null) {
            attributionDataDic.put("adgroup", attribution.adgroup);
        } else {
            attributionDataDic.put("adgroup", "");
        }

        // Creative
        if (attribution.creative != null) {
            attributionDataDic.put("creative", attribution.creative);
        } else {
            attributionDataDic.put("creative", "");
        }

        // Click label
        if (attribution.clickLabel != null) {
            attributionDataDic.put("clickLabel", attribution.clickLabel);
        } else {
            attributionDataDic.put("clickLabel", "");
        }

        return attributionDataDic;
    }
}
