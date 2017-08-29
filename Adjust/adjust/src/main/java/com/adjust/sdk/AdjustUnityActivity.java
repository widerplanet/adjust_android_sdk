package com.adjust.sdk;

import com.unity3d.player.UnityPlayerActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;

/**
 * Created by Obaied on 07/31/17.
 */

public class AdjustUnityActivity extends UnityPlayerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
