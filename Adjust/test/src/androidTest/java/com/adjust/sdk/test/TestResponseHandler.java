package com.adjust.sdk.test;

import android.content.*;
import android.net.*;
import android.os.*;
import android.test.*;

import com.adjust.sdk.*;

/**
 * Created by Abdullah on 02/08/16.
 */
public class TestResponseHandler extends ActivityInstrumentationTestCase2<UnitTestActivity> {
    private MockLogger mockLogger;
    private AssertUtil assertUtil;
    private UnitTestActivity activity;
    private Context context;
    private ResponseHandler responseHandler;

    public TestResponseHandler() {
        super(UnitTestActivity.class);
    }

    public TestResponseHandler(Class<UnitTestActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockLogger = new MockLogger();

        assertUtil = new AssertUtil(mockLogger);

        AdjustFactory.setLogger(mockLogger);

        activity = getActivity();
        context = activity.getApplicationContext();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        AdjustFactory.setLogger(null);
    }

//    public void checkFinishTasks(AdjustConfig config,
//                                 DelegatesPresent delegatesPresent)
//    {
//        ActivityHandler activityHandler = startAndCheckFirstSession(config);
//
//        // test first session package
//        ActivityPackage firstSessionPackage = mockPackageHandler.queue.get(0);
//
//        // create activity package test
//        TestActivityPackage testActivityPackage = new TestActivityPackage(firstSessionPackage);
//
//        testActivityPackage.needsResponseDetails =
//                delegatesPresent.attributionDelegatePresent ||
//                        delegatesPresent.eventSuccessDelegatePresent ||
//                        delegatesPresent.eventFailureDelegatePresent ||
//                        delegatesPresent.sessionSuccessDelegatePresent ||
//                        delegatesPresent.sessionFailureDelegatePresent;
//
//        // set first session
//        testActivityPackage.testSessionPackage(1);
//
//        // simulate a successful session
//        SessionResponseData successSessionResponseData = (SessionResponseData) ResponseData.buildResponseData(firstSessionPackage);
//        successSessionResponseData.success = true;
//
//        activityHandler.finishedTrackingActivity(successSessionResponseData);
//        SystemClock.sleep(1000);
//
//        // attribution handler should always receive the session response
//        assertUtil.test("AttributionHandler checkSessionResponse");
//        // the first session does not trigger the event response delegate
//
//        assertUtil.notInDebug("Launching success event tracking listener");
//        assertUtil.notInDebug("Launching failed event tracking listener");
//
//        activityHandler.launchSessionResponseTasks(successSessionResponseData);
//        SystemClock.sleep(1000);
//
//        // if present, the first session triggers the success session delegate
//        if (delegatesPresent.sessionSuccessDelegatePresent) {
//            assertUtil.debug("Launching success session tracking listener");
//        } else {
//            assertUtil.notInDebug("Launching success session tracking delegate");
//        }
//        // it doesn't trigger the failure session delegate
//        assertUtil.notInDebug("Launching failed session tracking listener");
//
//        // simulate a failure session
//        SessionResponseData failureSessionResponseData = (SessionResponseData)ResponseData.buildResponseData(firstSessionPackage);
//        failureSessionResponseData.success = false;
//
//        activityHandler.launchSessionResponseTasks(failureSessionResponseData);
//        SystemClock.sleep(1000);
//
//        // it doesn't trigger the success session delegate
//        assertUtil.notInDebug("Launching success session tracking listener");
//
//        // if present, the first session triggers the failure session delegate
//        if (delegatesPresent.sessionFailureDelegatePresent) {
//            assertUtil.debug("Launching failed session tracking listener");
//        } else {
//            assertUtil.notInDebug("Launching failed session tracking listener");
//        }
//
//        // test success event response data
//        activityHandler.trackEvent(new AdjustEvent("abc123"));
//        SystemClock.sleep(1000);
//
//        ActivityPackage eventPackage = mockPackageHandler.queue.get(1);
//        EventResponseData eventSuccessResponseData = (EventResponseData)ResponseData.buildResponseData(eventPackage);
//        eventSuccessResponseData.success = true;
//
//        activityHandler.finishedTrackingActivity(eventSuccessResponseData);
//        SystemClock.sleep(1000);
//
//        // attribution handler should never receive the event response
//        assertUtil.notInTest("AttributionHandler checkSessionResponse");
//
//        // if present, the success event triggers the success event delegate
//        if (delegatesPresent.eventSuccessDelegatePresent) {
//            assertUtil.debug("Launching success event tracking listener");
//        } else {
//            assertUtil.notInDebug("Launching success event tracking listener");
//        }
//        // it doesn't trigger the failure event delegate
//        assertUtil.notInDebug("Launching failed event tracking listener");
//
//        // test failure event response data
//        EventResponseData eventFailureResponseData = (EventResponseData)ResponseData.buildResponseData(eventPackage);
//        eventFailureResponseData.success = false;
//
//        activityHandler.finishedTrackingActivity(eventFailureResponseData);
//        SystemClock.sleep(1000);
//
//        // attribution handler should never receive the event response
//        assertUtil.notInTest("AttributionHandler checkSessionResponse");
//
//        // if present, the failure event triggers the failure event delegate
//        if (delegatesPresent.eventFailureDelegatePresent) {
//            assertUtil.debug("Launching failed event tracking listener");
//        } else {
//            assertUtil.notInDebug("Launching failed event tracking listener");
//        }
//        // it doesn't trigger the success event delegate
//        assertUtil.notInDebug("Launching success event tracking listener");
//
//        // test click
//        Uri attributions = Uri.parse("AdjustTests://example.com/path/inApp?adjust_tracker=trackerValue&other=stuff&adjust_campaign=campaignValue&adjust_adgroup=adgroupValue&adjust_creative=creativeValue");
//        long now = System.currentTimeMillis();
//
//        activityHandler.readOpenUrl(attributions, now);
//        SystemClock.sleep(1000);
//
//        assertUtil.test("PackageHandler addPackage");
//        assertUtil.test("PackageHandler sendFirstPackage");
//
//        // test sdk_click response data
//        ActivityPackage sdkClickPackage = mockSdkClickHandler.queue.get(0);
//        ClickResponseData clickResponseData = (ClickResponseData)ResponseData.buildResponseData(sdkClickPackage);
//
//        activityHandler.finishedTrackingActivity(clickResponseData);
//        SystemClock.sleep(1000);
//
//        // attribution handler should never receive the click response
//        assertUtil.notInTest("AttributionHandler checkSessionResponse");
//        // it doesn't trigger the any event delegate
//        assertUtil.notInDebug("Launching success event tracking listener");
//        assertUtil.notInDebug("Launching failed event tracking listener");
//    }



}
