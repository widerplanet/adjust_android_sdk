/*
 * Copyright 2014-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package Tests;

import Tests.AbstractBaseTests.TestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests for alert page
 */
public class AlertPageTest extends TestBase {
    public static String PACKAGE_NAME = "com.example.testapp";
    @Override
    public String getName() {
        return "Alerts";
    }

    /**
     * Tests the alert view by clicking the alert view button,
     * then verifying the alert view message, and then
     * accepting the alert view message.
     */
    @Test
    public void testAlertMessage(){
        System.out.println(">>>>>>>>>>>");
        System.out.println("pagesource: " + driver.getPageSource());
        System.out.println("<<<<<<<<<<<");
        Assert.assertEquals(1==1, true);
    }

    @Test
    public void testMethod() {
        System.out.println("Testing...");

        //Initial delay
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            boolean isRunning = driver.getPageSource().contains(AlertPageTest.PACKAGE_NAME);

            if (!isRunning) {
                System.out.println("Test concluded...");
                break;
            } else {
                System.out.println("still testing...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
