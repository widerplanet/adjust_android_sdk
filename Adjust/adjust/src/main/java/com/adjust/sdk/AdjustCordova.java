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
    private static String callbackId;
    private static CordovaWebView cordovaWebView;
    private static boolean isAttributionCallbackSet = false;

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {
        if (action.equals("create")) {
            JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, String> parameters = jsonObjectToMap(jsonParameters);

            String appToken = parameters.get("appToken");
            String environment = parameters.get("environment");
            String sdkPrefix = parameters.get("sdkPrefix");
            String defaultTracker = parameters.get("defaultTracker");
            String processName = parameters.get("processName");

            String logLevel = parameters.get("logLevel");
            String eventBufferingEnabled = parameters.get("eventBufferingEnabled");

            LogLevel logLevelValue;

            if (logLevel.equals("VERBOSE")) {
                logLevelValue = LogLevel.VERBOSE;
            } else if (logLevel.equals("DEBUG")) {
                logLevelValue = LogLevel.DEBUG;
            } else if (logLevel.equals("INFO")) {
                logLevelValue = LogLevel.INFO;
            } else if (logLevel.equals("WARN")) {
                logLevelValue = LogLevel.WARN;
            } else if (logLevel.equals("ERROR")) {
                logLevelValue = LogLevel.ERROR;
            } else if (logLevel.equals("ASSERT")) {
                logLevelValue = LogLevel.ASSERT;
            } else {
                logLevelValue = LogLevel.INFO;
            }

            boolean eventBufferingEnabledValue;

            if (eventBufferingEnabled.equals("NO")) {
                eventBufferingEnabledValue = false;
            } else if (eventBufferingEnabled.equals("YES")) {
                eventBufferingEnabledValue = true;
            } else {
                eventBufferingEnabledValue = false;
            }

            // Logger logger = (Logger)AdjustFactory.getLogger();
            // logger.info(String.format("App token = (%s)", appToken));
            // logger.info(String.format("Environment = (%s)", environment));
            // logger.info(String.format("Log level = (%s)", logLevel));
            // logger.info(String.format("SDK prefix = (%s)", sdkPrefix));
            // logger.info(String.format("Default tracker = (%s)", defaultTracker));
            // logger.info(String.format("Process name = (%s)", processName));
            // logger.info(String.format("Event buffering enabled = (%s)", eventBufferingEnabled));

            AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity(), appToken, environment);
            adjustConfig.setEventBufferingEnabled(eventBufferingEnabledValue);
            adjustConfig.setLogLevel(logLevelValue);

            if (sdkPrefix != null && !sdkPrefix.equals("") && !sdkPrefix.equals("null")) {
                adjustConfig.setSdkPrefix(sdkPrefix);
            }
            
            if (processName != null && !processName.equals("") && !processName.equals("null")) {
                adjustConfig.setProcessName(processName);
            }
            
            if (defaultTracker != null && !defaultTracker.equals("") && !defaultTracker.equals("null")) {
                adjustConfig.setDefaultTracker(defaultTracker);
            }
            
            if (isAttributionCallbackSet) {
                adjustConfig.setOnAttributionChangedListener(this);
            }

            Adjust.onCreate(adjustConfig);
            Adjust.onResume();

            return true;
        } else if (action.equals("setAttributionCallback")) {
            AdjustCordova.callbackId = callbackContext.getCallbackId();
            AdjustCordova.cordovaWebView = this.webView;

            isAttributionCallbackSet = true;
            // Adjust.setOnFinishedListener(this);

            return true;
        } else if (action.equals("trackEvent")) {
            String eventToken = args.getString(0);
            // JSONObject jsonParameters = args.optJSONObject(1);
            // if (jsonParameters == null) {
            //     Adjust.trackEvent(eventToken);
            // } else {
            //     Map<String, String> parameters = jsonObjectToMap(jsonParameters);
            //     Adjust.trackEvent(eventToken, parameters);
            // }
            AdjustEvent adjustEvent = new AdjustEvent(eventToken);
            Adjust.trackEvent(adjustEvent);

            return true;
        } else if (action.equals("trackRevenue")) {
            // double amountInCents = args.getDouble(0);
            // String eventToken = args.optString(1);
            // JSONObject jsonParameters = args.optJSONObject(2);

            // if (eventToken == "null" ||
            //     eventToken == null)
            // {
            //     Adjust.trackRevenue(amountInCents);
            // } else if (jsonParameters == null) {
            //     Adjust.trackRevenue(amountInCents, eventToken);
            // } else {
            //     Map<String, String> parameters = jsonObjectToMap(jsonParameters);
            //     Adjust.trackRevenue(amountInCents, eventToken, parameters);
            // }
            
            return true;
        } else if (action.equals("setFinishedTrackingCallback")) {
            // AdjustCordova.callbackId = callbackContext.getCallbackId();
            // AdjustCordova.cordovaWebView = this.webView;
            // Adjust.setOnFinishedListener(this);

            return true;
        } else if (action.equals("onPause")) {
            Adjust.onPause();

            return true;
        } else if (action.equals("onResume")) {
            // Adjust.onResume(this.cordova.getActivity());
            Adjust.onResume();

            return true;
        } else if (action.equals("setEnabled")) {
            Boolean enabled = args.getBoolean(0);
            Adjust.setEnabled(enabled);

            return true;
        } else if (action.equals("isEnabled")) {
            Boolean isEnabled = Adjust.isEnabled();
            PluginResult pluginResult = new PluginResult(Status.OK,
                    isEnabled);
            callbackContext.sendPluginResult(pluginResult);

            return true;
        }

        String errorMessage = String.format("Invalid call (%s)", action);

        Logger logger = (Logger)AdjustFactory.getLogger();
        logger.error(errorMessage);
        // callbackContext.error(errorMessage);

        return false;
    }

    // @Override
    // public void onFinishedTracking(ResponseData responseData) {
    //     // TODO Auto-generated method stub
        // JSONObject responseJsonData = new JSONObject(responseData.toDic());
        // PluginResult pluginResult = new PluginResult(Status.OK,
        //         responseJsonData);
        // pluginResult.setKeepCallback(true);

        // CallbackContext callbackResponseData = new CallbackContext(
        //         AdjustCordova.callbackId, AdjustCordova.cordovaWebView);
        // callbackResponseData.sendPluginResult(pluginResult);
    // }
    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);

        CallbackContext callbackResponseData = new CallbackContext(AdjustCordova.callbackId, AdjustCordova.cordovaWebView);
        callbackResponseData.sendPluginResult(pluginResult);
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
