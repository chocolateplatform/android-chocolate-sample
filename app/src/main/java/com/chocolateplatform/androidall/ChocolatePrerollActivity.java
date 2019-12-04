package com.chocolateplatform.androidall;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vdopia.ads.lw.ChocolateLogger;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.PreRollVideoAd;
import com.vdopia.ads.lw.PrerollAdListener;

/**
 * Activity to display Preroll full screen ads.
 *
 * @author Chocolate Platform Team
 */
public class ChocolatePrerollActivity extends Activity implements PrerollAdListener {

    private static final String TAG = "ChocolatePrerollActivity";

    private PreRollVideoAd preRollVideoAd;
    private FrameLayout frameLayout;
    private boolean isFullScreen;
    private boolean isLoaded;
    private boolean canGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChocolateLogger.i(TAG, "  onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 19)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        else
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frameLayout.setBackgroundColor(Color.TRANSPARENT);
        setContentView(frameLayout);
        preRollVideoAd = new PreRollVideoAd(this);
        LVDOAdRequest adRequest = new LVDOAdRequest(this);
        preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_320_480, this);

    }

    @Override
    public void onPrerollAdLoaded(View prerollAd) {
        ChocolateLogger.i(TAG, "onPrerollAdLoaded");
        if (preRollVideoAd.getWinningPartnerName().equals(LVDOConstants.PARTNER.CHOCOLATE)
              || preRollVideoAd.getWinningPartnerName().equals(LVDOConstants.PARTNER.GOOGLE)) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            //x = 1024 * 9 / 16   (1024 is the physical width)
            int height = (displayMetrics.widthPixels * 9) / 16;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
            params.gravity = Gravity.CENTER;
            preRollVideoAd.setLayoutParams(params);
            isFullScreen = true;
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            preRollVideoAd.setLayoutParams(params);
            isFullScreen = false;
        }
        frameLayout.addView(preRollVideoAd);
        preRollVideoAd.showAd();
        isLoaded = true;
        startBackTimeout();
        frameLayout.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onPrerollAdFailed(View prerollAd, LVDOConstants.LVDOErrorCode errorCode) {
        ChocolateLogger.i(TAG, "onPrerollAdFailed");
        Toast.makeText(this, "no fill", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPrerollAdShownError(View prerollAd) {
        ChocolateLogger.i(TAG, "onPrerollAdShownError");
        finish();
    }

    @Override
    public void onPrerollAdShown(View prerollAd) {
        ChocolateLogger.i(TAG, "onPrerollAdShown");
        /**
         * Yes, LOAD is correct
         */
    }

    @Override
    public void onPrerollAdClicked(View prerollAd) {
        ChocolateLogger.i(TAG, "onPrerollAdClicked");
    }

    @Override
    public void onPrerollAdCompleted(View prerollAd) {
        ChocolateLogger.i(TAG, "onPrerollAdCompleted");
        finish();
    }

    private boolean afterFirst = false;

    @Override
    protected void onResume() {
        super.onResume();
        ChocolateLogger.i(TAG, "  onResume");

        if (!afterFirst) {
            afterFirst = true;
        } else {
            if (isLoaded) {
                preRollVideoAd.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChocolateLogger.i(TAG, "  onPause");
        if (isLoaded) {
            preRollVideoAd.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChocolateLogger.i(TAG, "  onDestroy");
        if (isLoaded) {
            preRollVideoAd.destroyView();
        }
    }

    @Override
    public void onBackPressed() {
        if (canGoBack) {
            onPrerollAdCompleted(null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Build.VERSION.SDK_INT >= 19)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        else
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (isLoaded) {
            if (isFullScreen) {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ChocolateLogger.i(TAG, "  onConfigurationChanged PORTRAIT");
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    //x = 1024 * 9 / 16   (1024 is the physical width)
                    int height = (displayMetrics.widthPixels * 9) / 16;
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
                    params.gravity = Gravity.CENTER;
                    preRollVideoAd.getLayoutParams().width = displayMetrics.widthPixels;
                    preRollVideoAd.getLayoutParams().height = height;
                } else {
                    ChocolateLogger.i(TAG, "  onConfigurationChanged LANDSCAPE");
                    preRollVideoAd.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
                    preRollVideoAd.getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
                }
            }
        }
    }

    private void startBackTimeout() {
        new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                canGoBack = true;
            }
        }.start();
    }

}
