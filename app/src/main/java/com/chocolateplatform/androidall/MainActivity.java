package com.chocolateplatform.androidall;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private LVDOBannerAd bannerAd;
    private PreRollVideoAd preRollVideoAd;
    private ViewGroup adContainer;

    private VideoHelper videoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adContainer = findViewById(R.id.adContainer);

        //Be sure to set these values into the ad request.
        adRequest = new LVDOAdRequest(this);
        adRequest.setAppStoreUrl("https://play.google.com/store/apps/details?id=com.democompany.android");
        adRequest.setRequester("Chocolate Demo Company");
        adRequest.setAppDomain("http://democompany.com/test");
        adRequest.setAppName("Chocolate Demo");
        adRequest.setCategory("IAB2");
        adRequest.setPublisherDomain("http://democompany.com/");

        Chocolate.enableLogging(true);  //don't set true for production
        Chocolate.enableChocolateTestAds(true);  //don't set true for production
        Chocolate.init(this, API_KEY, adRequest, new InitCallback() {
            @Override
            public void onSuccess() {
                //Do not pre-fetch ads here.  Chocolate does it automatically, internally.
                //This callback simply lets you know it was fine.
            }

            @Override
            public void onError(String initError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (interstitialAd != null)  {
            interstitialAd.onResume();
        }
        if (rewardedAd != null) {
            rewardedAd.onResume();
        }
        if (bannerAd != null) {
            bannerAd.onResume();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.onResume();
        }
        if (videoHelper != null) {
            videoHelper.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (interstitialAd != null)  {
            interstitialAd.onPause();
        }
        if (rewardedAd != null) {
            rewardedAd.onPause();
        }
        if (bannerAd != null) {
            bannerAd.onPause();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.onPause();
        }
        if (videoHelper != null) {
            videoHelper.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAd != null)  {
            interstitialAd.destroyView();
        }
        if (rewardedAd != null) {
            rewardedAd.destroyView();
        }
        if (bannerAd != null) {
            bannerAd.destroyView();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.destroyView();
        }
        if (videoHelper != null)
            videoHelper.cleanUp();
    }

    /**
     * Note: In production release, simply call:
     *
     * interstitialAd.loadAd( adRequest )
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadInterstitialAd(View view) {

        interstitialAd = new LVDOInterstitialAd(this, this);
        //interstitialAd.loadAd(adRequest);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INTERSTITIAL, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInterstitialPartners(adRequest);
                interstitialAd.loadAd(adRequest);
            }
        });
    }

    /**
     * Note: In production release, simply call:
     *
     * rewardedAd.loadAd( adRequest )
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadRewardedAd(View view) {

        rewardedAd = new LVDORewardedAd(this, this);
        //rewardedAd.loadAd(adRequest);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_REWARDED, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setRewardedPartners(adRequest);
                rewardedAd.loadAd(adRequest);
            }
        });
    }

    /**
     * Note: In production release, simply call:
     *
     * bannerAd.loadAd( adRequest )
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadBannerAd(View view) {

        if (bannerAd != null)
            bannerAd.destroyView();

        setAdContainerSize(true);
        bannerAd = new LVDOBannerAd(this, LVDOAdSize.BANNER_320_50, this);
        //bannerAd.loadAd(adRequest);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INVIEW, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInviewPartners(adRequest);
                bannerAd.loadAd(adRequest);
            }
        });
    }

    public void loadBannerAdMREC(View view) {

        if (bannerAd != null)
            bannerAd.destroyView();

        setAdContainerSize(false);
        bannerAd = new LVDOBannerAd(this, LVDOAdSize.MEDIUM_RECT_300_250, this);
        //bannerAd.loadAd(adRequest);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_INVIEW, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setInviewPartners(adRequest);
                bannerAd.loadAd(adRequest);
            }
        });
    }


    /**
     * Note: In production release, simply call:
     *
     * preRollVideoAd.loadAd( adRequest )
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadPrerollAd(View view) {

        if (preRollVideoAd != null)
            preRollVideoAd.destroyView();

        setAdContainerSize(false);
        preRollVideoAd = new PreRollVideoAd(this);
        //preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_320_480, MainActivity.this);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_PREROLL, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setPrerollPartners(adRequest);
                preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_320_480, MainActivity.this);
            }
        });
    }

    public void loadPrerollAdFullscreen(View view) {

        if (preRollVideoAd != null)
            preRollVideoAd.destroyView();

        setAdContainerSize(false);
        preRollVideoAd = new PreRollVideoAd(this);
        //preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_FULLSCREEN, MainActivity.this);

        ChocolatePartners.choosePartners(ChocolatePartners.ADTYPE_PREROLL, this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChocolatePartners.setPrerollPartners(adRequest);
                preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_FULLSCREEN, MainActivity.this);
            }
        });
    }

    @Override
    public void onBannerAdLoaded(View view) {

        if (preRollVideoAd != null)
            preRollVideoAd.destroyView();

        ((ViewGroup)findViewById(R.id.adContainer)).removeAllViews();
        ((ViewGroup)findViewById(R.id.adContainer)).addView(view);
        ((TextView)findViewById(R.id.textView)).setText("Banner winner: " + bannerAd.getWinningPartnerName());
    }

    @Override
    public void onBannerAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Banner No-Fill");
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

        if (bannerAd != null)
            bannerAd.destroyView();
        //'view' param is the preRollVideoAd instance FYI!

        ((TextView)findViewById(R.id.textView)).setText("PreRoll Ad winner: " + preRollVideoAd.getWinningPartnerName());
        ((ViewGroup)findViewById(R.id.adContainer)).removeAllViews();

        ((ViewGroup)findViewById(R.id.adContainer)).addView(preRollVideoAd);
        //((ViewGroup)findViewById(R.id.adContainer)).addView(view);  //same as above !

        preRollVideoAd.showAd();
        //((PreRollVideoAd)view).showAd();  same as above!
    }

    @Override
    public void onPrerollAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView)findViewById(R.id.textView)).setText("Preroll No-Fill");
    }

    @Override
    public void onPrerollAdShown(View view) {

    }

    @Override
    public void onPrerollAdShownError(View view) {

    }

    @Override
    public void onPrerollAdClicked(View view) {

    }

    @Override
    public void onPrerollAdCompleted(View view) {

        //Let's pretend you want to roll a movie/video when the preroll ad is completed.
        videoHelper = new VideoHelper(this, findViewById(R.id.adContainer));
        videoHelper.playContentVideo(0);
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

    private void setAdContainerSize(boolean isBanner) {
        if (isBanner) {
            adContainer.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.banner_width);
            adContainer.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.banner_height);
        } else {
            adContainer.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.mrec_width);
            adContainer.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.mrec_height);
        }
    }
}
