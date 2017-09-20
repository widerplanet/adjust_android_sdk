package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.adjust.sdk.AdjustFactory;
import com.adjust.sdk.Constants;
import com.adjust.sdk.UtilNetworking;
import com.adjust.testlibrary.TestLibrary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.testapp.CommandListener.debug;

public class MainActivity extends AppCompatActivity {
    public static TestLibrary testLibrary;
    public static final String TAG = "TestApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommandListener commandListener = new CommandListener(this.getApplicationContext());
//        String baseUrl = "https://10.0.2.2:8443";
        String baseUrl = "https://jenkins-1.adjust.com:8443";
        AdjustFactory.setTestingMode(baseUrl);
        testLibrary = new TestLibrary(baseUrl, commandListener);
//        testLibrary.setTests("current/Test_Event_Count;current/Test_Event_OrderId;current/pre4120/Test_Disable_Enable;current/pre4120/Test_EventBuffering;current/Test_DefaultTracker;current/Test_DelayStart");
        startTestSession();
    }

    private void startTestSession() {
        //testLibrary.setTests("Test_Event;Test_Install_Attribution");
        //testLibrary.doNotExitAfterEnd();
        testLibrary.initTestSession("android4.11.4");
        //testLibrary.initTest("android4.11.4", "Test_Event");
    }
}
