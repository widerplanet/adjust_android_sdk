//
//  Util.java
//  Adjust
//
//  Created by Christian Wellenbrock on 2012-10-11.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//  See the file MIT-LICENSE for copying permission.
//

package com.adjust.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static com.adjust.sdk.Constants.ENCODING;
import static com.adjust.sdk.Constants.MD5;
import static com.adjust.sdk.Constants.SHA1;

/**
 * Collects utility functions used by Adjust.
 */
public class Util {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";
    private static final String fieldReadErrorMessage = "Unable to read '%s' field in migration device with message (%s)";
    public static final DecimalFormat SecondsDisplayFormat = new DecimalFormat("0.0");
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    protected static String createUuid() {
        return UUID.randomUUID().toString();
    }

    public static String readStringField(ObjectInputStream.GetField fields, String name, String defaultValue) {
        return readObjectField(fields, name, defaultValue);
    }

    public static <T> T readObjectField(ObjectInputStream.GetField fields, String name, T defaultValue) {
        try {
            return (T) fields.get(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean readBooleanField(ObjectInputStream.GetField fields, String name, boolean defaultValue) {
        try {
            return fields.get(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int readIntField(ObjectInputStream.GetField fields, String name, int defaultValue) {
        try {
            return fields.get(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long readLongField(ObjectInputStream.GetField fields, String name, long defaultValue) {
        try {
            return fields.get(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean equalObject(Object first, Object second) {
        if (first == null || second == null) {
            return first == null && second == null;
        }
        return first.equals(second);
    }

    public static boolean equalsDouble(Double first, Double second) {
        if (first == null || second == null) {
            return first == null && second == null;
        }
        return Double.doubleToLongBits(first) == Double.doubleToLongBits(second);
    }

    public static boolean equalString(String first, String second) {
        return equalObject(first, second);
    }

    public static boolean equalEnum(Enum first, Enum second) {
        return equalObject(first, second);
    }

    public static boolean equalLong(Long first, Long second) {
        return equalObject(first, second);
    }

    public static boolean equalInt(Integer first, Integer second) {
        return equalObject(first, second);
    }

    public static boolean equalBoolean(Boolean first, Boolean second) {
        return equalObject(first, second);
    }

    public static int hashBoolean(Boolean value) {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }

    public static int hashLong(Long value) {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }

    public static int hashString(String value) {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }

    public static int hashEnum(Enum value) {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }

    public static int hashObject(Object value) {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }
}
