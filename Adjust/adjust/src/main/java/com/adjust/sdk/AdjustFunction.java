package com.adjust.sdk;

import android.util.Log;
import com.adobe.fre.*;

/**
 * Created by pfms on 31/07/14.
 */
public class AdjustFunction implements FREFunction, OnAttributionChangedListener {
    private static String sdkPrefix = "adobe_air4.0.0";

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
            this.freContext = freContext;

            String appToken = freObjects[0].getAsString();
            String environment = freObjects[1].getAsString();

            if (appToken != null && environment != null) {
                AdjustConfig adjustConfig = new AdjustConfig(freContext.getActivity(), appToken, environment);
                adjustConfig.setSdkPrefix(sdkPrefix);

                if (freObjects[2] != null) {
                    String logLevel = freObjects[2].getAsString();

                    if (logLevel != null) {
                        if (logLevel.equals("verbose")) {
                            adjustConfig.setLogLevel(LogLevel.VERBOSE);
                        } else if (logLevel.equals("debug")) {
                            adjustConfig.setLogLevel(LogLevel.DEBUG);
                        } else if (logLevel.equals("info")) {
                            adjustConfig.setLogLevel(LogLevel.INFO);
                        } else if (logLevel.equals("warn")) {
                            adjustConfig.setLogLevel(LogLevel.WARN);
                        } else if (logLevel.equals("error")) {
                            adjustConfig.setLogLevel(LogLevel.ERROR);
                        } else if (logLevel.equals("assert")) {
                            adjustConfig.setLogLevel(LogLevel.ASSERT);
                        } else {
                            adjustConfig.setLogLevel(LogLevel.INFO);
                        }
                    }
                }

                if (freObjects[3] != null) {
                    Boolean eventBuffering = freObjects[3].getAsBool();
                    adjustConfig.setEventBufferingEnabled(eventBuffering);
                }

                if (freObjects[4] != null) {
                    Boolean isAttributionCallbackSet = freObjects[4].getAsBool();

                    if (isAttributionCallbackSet) {
                        adjustConfig.setOnAttributionChangedListener(this);
                    }
                }

                if (freObjects[5] != null) {
                    String defaultTracker = freObjects[5].getAsString();

                    if (defaultTracker != null) {
                        adjustConfig.setDefaultTracker(defaultTracker);
                    }
                }

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

    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        String response = "trackerToken=" + attribution.trackerToken + ","
                + "trackerName=" + attribution.trackerName + ","
                + "campaign=" + attribution.campaign + ","
                + "network=" + attribution.network + ","
                + "creative=" + attribution.creative + ","
                + "adgroup=" + attribution.adgroup + ","
                + "clickLabel=" + attribution.clickLabel;

        this.freContext.dispatchStatusEventAsync("adjust_attributionData", response);
    }
}