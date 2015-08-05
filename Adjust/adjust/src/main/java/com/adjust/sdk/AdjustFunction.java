package com.adjust.sdk;

import android.util.Log;
import com.adobe.fre.*;

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
            return SetEnabled(freContext, freObjects);
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

            if (appToken != null && environment != null) {
                AdjustConfig adjustConfig = new AdjustConfig(freContext.getActivity(), appToken, environment);
                adjustConfig.setLogLevel(LogLevel.VERBOSE);
                adjustConfig.setSdkPrefix("adobe_air4.0.0");

                Adjust.onCreate(adjustConfig);
            }
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject TrackEvent(FREContext freContext, FREObject[] freObjects) {
        try {
            String eventToken = freObjects[0].getAsString();
            String currency = freObjects[1].getAsString();
            double revenue = freObjects[2].getAsDouble();

            if (eventToken != null) {
                AdjustEvent adjustEvent = new AdjustEvent(eventToken);

                if (currency != null) {
                    adjustEvent.setRevenue(revenue, currency);
                }

                if (freObjects[3] != null) {
                    for (int i = 0; i < ((FREArray) freObjects[3]).getLength(); i += 2) {
                        adjustEvent.addCallbackParameter(((FREArray) freObjects[3]).getObjectAt(i).getAsString(),
                                ((FREArray) freObjects[3]).getObjectAt(i + 1).getAsString());
                    }
                }

                if (freObjects[4] != null) {
                    for (int i = 0; i < ((FREArray) freObjects[4]).getLength(); i += 2) {
                        adjustEvent.addCallbackParameter(((FREArray) freObjects[4]).getObjectAt(i).getAsString(),
                                ((FREArray) freObjects[4]).getObjectAt(i + 1).getAsString());
                    }
                }

                Adjust.trackEvent(adjustEvent);
            }
        } catch (Exception e) {
            Log.e(AdjustExtension.LogTag, e.getMessage());
        }
        return null;
    }

    private FREObject SetEnabled(FREContext freContext, FREObject[] freObjects) {
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
}