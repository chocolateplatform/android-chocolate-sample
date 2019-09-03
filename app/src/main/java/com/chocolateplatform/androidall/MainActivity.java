package com.chocolateplatform.androidall;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.device.ads.AdRegistration;
import com.vdopia.ads.lw.Chocolate;
import com.vdopia.ads.lw.InitCallback;
import com.vdopia.ads.lw.LVDOAdRequest;
import com.vdopia.ads.lw.LVDOAdSize;
import com.vdopia.ads.lw.LVDOBannerAd;
import com.vdopia.ads.lw.LVDOBannerAdListener;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.LVDOInterstitialAd;
import com.vdopia.ads.lw.LVDOInterstitialListener;
import com.vdopia.ads.lw.LVDORewardedAd;
import com.vdopia.ads.lw.PreRollVideoAd;
import com.vdopia.ads.lw.PrerollAdListener;
import com.vdopia.ads.lw.RewardedAdListener;

public class MainActivity extends AppCompatActivity implements RewardedAdListener, LVDOInterstitialListener, LVDOBannerAdListener, PrerollAdListener {

    static String API_KEY = "XqjhRR";

    private LVDOAdRequest adRequest;
    private LVDORewardedAd rewardedAd;
    private LVDOInterstitialAd interstitialAd;
    private LVDOBannerAd inviewAd;
    private PreRollVideoAd preRollVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdRegistration.enableTesting(true); //amazon
        
        adRequest = new LVDOAdRequest(this);
        Chocolate.enableLogging(true);  //don't set for production
        Chocolate.enableChocolateTestAds(true);  //don't set for production
        //Chocolate.setAutoClose(true); //only set true for AirKast
        Chocolate.init(this, API_KEY, adRequest, new InitCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String s) {

            }
        });
        rewardedAd = new LVDORewardedAd(this, this);
        interstitialAd = new LVDOInterstitialAd(this, this);
        inviewAd = new LVDOBannerAd(this, this);
        preRollVideoAd = new PreRollVideoAd(this);
    }

    public void loadInterstitialAd(View view) {
        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INTERSTITIAL, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInterstitialPartners(adRequest);
                interstitialAd.loadAd(adRequest);
            }
        });
    }

    public void loadRewardedAd(View view) {
        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_REWARDED, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setRewardedPartners(adRequest);
                rewardedAd.loadAd(adRequest);
            }
        });
    }

    public void loadInviewAd(View view) {
        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INVIEW, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInviewPartners(adRequest);
                inviewAd.loadAd(adRequest);
            }
        });
    }

    public void loadPrerollAd(View view) {
        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_PREROLL, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setPrerollPartners(adRequest);
                preRollVideoAd.loadAd(adRequest, LVDOAdSize.PRE_ROLL, MainActivity.this);
            }
        });
    }

    @Override
    public void onBannerAdLoaded(View view) {
        ((ViewGroup)findViewById(R.id.adContainer)).removeAllViews();
        ((ViewGroup)findViewById(R.id.adContainer)).addView(view);
        ((TextView)findViewById(R.id.textView)).setText("Inview winner: " + inviewAd.getWinningPartnerName());
    }

    @Override
    public void onBannerAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Inview No-Fill");
    }

    @Override
    public void onBannerAdClicked(View view) {

    }

    @Override
    public void onBannerAdClosed(View view) {

    }

    @Override
    public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
        interstitialAd.show();
        ((TextView)findViewById(R.id.textView)).setText("Interstitial Ad winner: " + interstitialAd.getWinningPartnerName());
    }

    @Override
    public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Interstitial No-Fill");
    }

    @Override
    public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {

    }

    @Override
    public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {

    }

    @Override
    public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {

    }

    @Override
    public void onPrerollAdLoaded(View view) {
        ((TextView)findViewById(R.id.textView)).setText("PreRoll Ad winner: " + preRollVideoAd.getWinningPartnerName());
        ((ViewGroup)findViewById(R.id.adContainer)).removeAllViews();
        ((ViewGroup)findViewById(R.id.adContainer)).addView(preRollVideoAd);
        preRollVideoAd.showAd();
    }

    @Override
    public void onPrerollAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Preroll No-Fill");
    }

    @Override
    public void onPrerollAdShown(View view) {

    }

    @Override
    public void onPrerollAdClicked(View view) {

    }

    @Override
    public void onPrerollAdCompleted(View view) {

    }

    @Override
    public void onRewardedVideoLoaded(LVDORewardedAd lvdoRewardedAd) {
        lvdoRewardedAd.showRewardAd("my secret code", "my userid", "V-BUCKS", "5000");
        ((TextView)findViewById(R.id.textView)).setText("Rewarded Ad winner: " + rewardedAd.getWinningPartnerName());
    }

    @Override
    public void onRewardedVideoFailed(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Rewarded No-Fill");
    }

    @Override
    public void onRewardedVideoShown(LVDORewardedAd lvdoRewardedAd) {

    }

    @Override
    public void onRewardedVideoShownError(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Rewarded Got Fill, but Error Showing");
    }

    @Override
    public void onRewardedVideoDismissed(LVDORewardedAd lvdoRewardedAd) {

    }

    @Override
    public void onRewardedVideoCompleted(LVDORewardedAd lvdoRewardedAd) {

    }
}
