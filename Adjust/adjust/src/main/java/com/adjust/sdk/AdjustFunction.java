package com.adjust.sdk;

import android.util.Log;
import com.adobe.fre.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pfms on 31/07/14.
 */
public class AdjustFunction implements FREFunction {
    private String functionName;
    private FREContext freContext;

    public AdjustFunction(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        if (functionName == AdjustContext.OnCreate) {
            return OnCreate(freContext, freObjects);
        }
        if (functionName == AdjustContext.TrackEvent) {
            return TrackEvent(freContext, freObjects);
        }
        if (functionName == AdjustContext.SetEnabled) {
            return SetEnable(freContext, freObjects);
        }
        if (functionName == AdjustContext.IsEnabled) {
            return IsEnabled(freContext, freObjects);
        }
        if (functionName == AdjustContext.OnResume) {
            return OnResume(freContext, freObjects);
        }
        if (functionName == AdjustContext.OnPause) {
            return OnPause(freContext, freObjects);
        }

        return null;
    }

    private FREObject OnCreate(FREContext freContext, FREObject[] freObjects) {
        try {
            String appToken = freObjects[0].getAsString();
            String environment = freObjects[1].getAsString();
            String logLevel = freObjects[2].getAsString();
            Boolean eventBuffering = freObjects[3].getAsBool();

            AdjustConfig adjustConfig = new AdjustConfig(freContext.getActivity(), appToken, environment);
            adjustConfig.setLogLevel(LogLevel.VERBOSE);
            adjustConfig.setSdkPrefix("adobe_air4.0.0");

            Adjust.onCreate(adjustConfig);
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject TrackEvent(FREContext freContext, FREObject[] freObjects) {
        try {
            String eventToken = freObjects[0].getAsString();

            // Map<String, String> parameters = getAsMap(freObjects[1]);

            AdjustEvent adjustEvent = new AdjustEvent(eventToken);

            Adjust.trackEvent(adjustEvent);
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject SetEnable(FREContext freContext, FREObject[] freObjects) {
        try {
            Boolean enabled = freObjects[0].getAsBool();

            Adjust.setEnabled(enabled);
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject IsEnabled(FREContext freContext, FREObject[] freObjects) {
        try {
            return FREObject.newObject(Adjust.isEnabled());
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject OnResume(FREContext freContext, FREObject[] freObjects) {
        try {
            Adjust.onResume();
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject OnPause(FREContext freContext, FREObject[] freObjects) {
        try {
            Adjust.onPause();
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private Map<String, String> getAsMap(FREObject freObject) throws Exception{
        if (freObject == null) {
            return null;
        }
        FREArray parameters = (FREArray) freObject.getProperty("adjust keys");

        if (parameters == null) {
            Log.e(AdjustExtension.LogTag, "getAsMap property 'adjust keys' is null");
            return null;
        }

        int i = 0;
        int length = (int)parameters.getLength();

        Map<String, String> map = new HashMap<String, String>(length);

        while (i < length) {
            String key = parameters.getObjectAt(i).getAsString();
            String value = freObject.getProperty(key).getAsString();
            map.put(key,value);

            i++;
        }

        return map;
    }
}