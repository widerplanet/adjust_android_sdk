package com.adjust.sdk;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.adjust.sdk.test.UnitTestActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by pfms on 07/09/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestFiles {
    protected MockLogger mockLogger;
    protected MockPackageHandler mockPackageHandler;
    protected MockAttributionHandler mockAttributionHandler;
    protected MockSdkClickHandler mockSdkClickHandler;
    protected com.adjust.sdk.test.UnitTestActivity activity;
    protected Context context;
    protected AssertUtil assertUtil;

    @Rule
    public ActivityTestRule<UnitTestActivity> mActivityRule = new ActivityTestRule(com.adjust.sdk.test.UnitTestActivity.class);

    @Before
    public void setUp() {
        mockLogger = new MockLogger();
        mockPackageHandler = new MockPackageHandler(mockLogger);
        mockAttributionHandler = new MockAttributionHandler(mockLogger);
        mockSdkClickHandler = new MockSdkClickHandler(mockLogger);
        assertUtil = new AssertUtil(mockLogger);

        AdjustFactory.setLogger(mockLogger);
        AdjustFactory.setPackageHandler(mockPackageHandler);
        AdjustFactory.setAttributionHandler(mockAttributionHandler);
        AdjustFactory.setSdkClickHandler(mockSdkClickHandler);

        //activity = launchActivity(null);
        activity = mActivityRule.getActivity();
        context = activity.getApplicationContext();

        // deleting state to simulate fresh install
        mockLogger.test("Was AdjustActivityState deleted? " + ActivityHandler.deleteActivityState(context));
        mockLogger.test("Was Attribution deleted? " + ActivityHandler.deleteAttribution(context));
        mockLogger.test("Was Session Callback Parameters deleted? " + ActivityHandler.deleteSessionCallbackParameters(context));
        mockLogger.test("Was Session Partner Parameters deleted? " + ActivityHandler.deleteSessionPartnerParameters(context));

        // check the server url
        assertUtil.isEqual(Constants.BASE_URL, "https://app.adjust.com");
    }

    @After
    public void tearDown() {
        AdjustFactory.setPackageHandler(null);
        AdjustFactory.setAttributionHandler(null);
        AdjustFactory.setSdkClickHandler(null);
        AdjustFactory.setLogger(null);
        AdjustFactory.setTimerInterval(-1);
        AdjustFactory.setTimerStart(-1);
        AdjustFactory.setSessionInterval(-1);
        AdjustFactory.setSubsessionInterval(-1);

        activity = null;
        context = null;
    }

    @Test
    public void testFile1() {
        ActivityStateV401 activityStateV401 = new ActivityStateV401();

        Util.writeObject(activityStateV401, context, "AdjustIoActivityState", "Activity state");

        //ActivityStateV490 activityStateV490 = (ActivityStateV490) Util.readObject(context, "AdjustIoActivityState", "Activity state", ActivityStateV401.class);

        assertUtil.fail();
    }

}
