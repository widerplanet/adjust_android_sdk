package com.adjust.sdk;

import android.content.*;
import android.os.*;
import android.util.*;

import static com.adjust.sdk.Constants.ACTIVITY_STATE_FILENAME;
import static com.adjust.sdk.Constants.ATTRIBUTION_FILENAME;
import static com.adjust.sdk.Constants.LOGTAG;

/**
 * Created by abdullah on 8/2/16.
 */
public class ResponseHandler extends HandlerThread implements IResponseHandler {
    private Handler internalHandler;
    private ILogger logger;

    private Context context;
    private OnEventTrackingSucceededListener onEventTrackingSucceededListener;
    private OnEventTrackingFailedListener onEventTrackingFailedListener;

    public ResponseHandler(Context context,
                           OnEventTrackingSucceededListener onEventTrackingSucceededListener,
                           OnEventTrackingFailedListener onEventTrackingFailedListener) {
        super("ResponseHandler", MIN_PRIORITY);
        setDaemon(true);
        start();

        this.logger = AdjustFactory.getLogger();
        this.internalHandler = new Handler(getLooper());

        this.context = context;
        this.onEventTrackingSucceededListener = onEventTrackingSucceededListener;
        this.onEventTrackingFailedListener = onEventTrackingFailedListener;
    }

    @Override
    public void finishedTrackingActivity(ResponseData responseData) {
        // check if it's an event response
        if (responseData instanceof EventResponseData) {
            launchEventResponseTasks((EventResponseData) responseData);
        }
    }

    private void launchEventResponseTasks(final EventResponseData eventResponseData) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                launchEventResponseTasksInternal(eventResponseData);
            }
        });
    }

    private void launchEventResponseTasksInternal(final EventResponseData eventResponseData) {
        Handler handler = new Handler(this.context.getMainLooper());

        // success callback
        if (eventResponseData.success && this.onEventTrackingSucceededListener != null) {
            logger.debug("Launching success event tracking listener");
            // add it to the handler queue
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ResponseHandler.this.onEventTrackingSucceededListener.onFinishedEventTrackingSucceeded(eventResponseData.getSuccessResponseData());
                }
            };
            handler.post(runnable);

            return;
        }
        // failure callback
        if (!eventResponseData.success && this.onEventTrackingFailedListener != null) {
            logger.debug("Launching failed event tracking listener");
            // add it to the handler queue
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ResponseHandler.this.onEventTrackingFailedListener.onFinishedEventTrackingFailed(eventResponseData.getFailureResponseData());
                }
            };
            handler.post(runnable);
        }
    }
}
