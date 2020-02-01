package com.chocolateplatform.androidall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements RewardedAdListener, LVDOInterstitialListener, PrerollAdListener {

    //private static final String API_KEY = "MwLJU6"; //anurag's key
    private static final String API_KEY = "XqjhRR";
    private static final String TAG = "MainActivity";
    private static final boolean DO_CHOOSE_PARTNERS = true; //purely for demonstration purposes.  set false later.

    private LVDOAdRequest adRequest;
    private LVDORewardedAd rewardedAd;
    private LVDOInterstitialAd interstitialAd;
    private LVDOBannerAd bannerAd;
    private LVDOBannerAd mrecBannerAd;
    private PreRollVideoAd preRollVideoAd;
    private boolean doPrerollAdFragment;

    private VideoHelper videoHelper;
    private boolean isLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLargeLayout = getResources().getBoolean(R.bool.large_layout);
        adRequest = new LVDOAdRequest(this);
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
        if (interstitialAd != null) {
            interstitialAd.onResume();
        }
        if (rewardedAd != null) {
            rewardedAd.onResume();
        }
        if (bannerAd != null) {
            bannerAd.onResume();
        }
        if (mrecBannerAd != null) {
            mrecBannerAd.onResume();
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
        if (interstitialAd != null) {
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
        if (interstitialAd != null) {
            interstitialAd.destroyView();
        }
        if (rewardedAd != null) {
            rewardedAd.destroyView();
        }
        if (bannerAd != null) {
            bannerAd.destroyView();
        }
        if (mrecBannerAd != null) {
            mrecBannerAd.destroyView();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.destroyView();
        }
        if (videoHelper != null)
            videoHelper.cleanUp();
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * interstitialAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadInterstitialAd(View view) {

        interstitialAd = new LVDOInterstitialAd(this, this);

        if (DO_CHOOSE_PARTNERS) {
            ChocolatePartners.choosePartners(this, adRequest, ChocolatePartners.ADTYPE_INTERSTITIAL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interstitialAd.loadAd(adRequest);
                }
            });
        } else {
            interstitialAd.loadAd(adRequest);
        }
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * rewardedAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadRewardedAd(View view) {

        rewardedAd = new LVDORewardedAd(this, this);

        if (DO_CHOOSE_PARTNERS) {
            ChocolatePartners.choosePartners(this, adRequest, ChocolatePartners.ADTYPE_REWARDED, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rewardedAd.loadAd(adRequest);
                }
            });
        } else {
            rewardedAd.loadAd(adRequest);
        }
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * bannerAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view
     */
    public void loadBannerAd(View view) {

        if (bannerAd != null)
            bannerAd.destroyView();

        bannerAd = new LVDOBannerAd(this, LVDOAdSize.BANNER_320_50, new LVDOBannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view) {
                ((ViewGroup) findViewById(R.id.banner_container)).removeAllViews();
                ((ViewGroup) findViewById(R.id.banner_container)).addView(view);
                ((TextView) findViewById(R.id.textView)).setText("Banner winner: " + bannerAd.getWinningPartnerName());
            }

            @Override
            public void onBannerAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
                ((TextView) findViewById(R.id.textView)).setText("Banner No-Fill");
            }

            @Override
            public void onBannerAdClicked(View view) {

            }

            @Override
            public void onBannerAdClosed(View view) {

            }
        });

        if (DO_CHOOSE_PARTNERS) {
            ChocolatePartners.choosePartners(this, adRequest, ChocolatePartners.ADTYPE_INVIEW, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bannerAd.loadAd(adRequest);
                }
            });
        } else {
            bannerAd.loadAd(adRequest);

        }
    }

    public void loadBannerAdMREC(View view) {

        if (mrecBannerAd != null)
            mrecBannerAd.destroyView();

        mrecBannerAd = new LVDOBannerAd(this, LVDOAdSize.MEDIUM_RECT_300_250, new LVDOBannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view) {

                if (preRollVideoAd != null)
                    preRollVideoAd.destroyView();

                ((ViewGroup) findViewById(R.id.mrec_container)).removeAllViews();
                ((ViewGroup) findViewById(R.id.mrec_container)).addView(view);
                ((TextView) findViewById(R.id.textView)).setText("MREC Banner winner: " + mrecBannerAd.getWinningPartnerName());
            }

            @Override
            public void onBannerAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
                ((TextView) findViewById(R.id.textView)).setText("MREC Banner No-Fill");
            }

            @Override
            public void onBannerAdClicked(View view) {

            }

            @Override
            public void onBannerAdClosed(View view) {

            }
        });

        if (DO_CHOOSE_PARTNERS) {
            ChocolatePartners.choosePartners(this, adRequest, ChocolatePartners.ADTYPE_INVIEW, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mrecBannerAd.loadAd(adRequest);
                }
            });
        } else {
            mrecBannerAd.loadAd(adRequest);
        }

    }

    private void loadPrerollAd() {
        cleanupPreroll();

        preRollVideoAd = new PreRollVideoAd(this);

        if (DO_CHOOSE_PARTNERS) {
            ChocolatePartners.choosePartners(this, adRequest, ChocolatePartners.ADTYPE_PREROLL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_320_480, MainActivity.this);
                }
            });
        } else {
            preRollVideoAd.loadAd(adRequest, LVDOAdSize.PREROLL_320_480, MainActivity.this);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            switch (resultCode) {
                case ChocolatePrerollActivity.RESULT_AD_COMPLETED:
                    playUserContent();
                    break;
                case ChocolatePrerollActivity.RESULT_AD_ERROR:
                    Toast.makeText(this, "ad error: could not play ad", Toast.LENGTH_SHORT).show();
                    break;
                case ChocolatePrerollActivity.RESULT_NO_FILL:
                    Toast.makeText(this, "full screen preroll NO-FILL", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    public void loadPrerollAdAsView(View view) {

        doPrerollAdFragment = false;
        loadPrerollAd();
    }

    public void loadPrerollAdAsFragment(View view) {

        doPrerollAdFragment = true;
        loadPrerollAd();
    }

    public void loadPrerollAdAsActivity(View view) {
        cleanupPreroll();
        startActivityForResult(new Intent(this, ChocolatePrerollActivity.class), 10);
    }

    private void cleanupPreroll() {
        if (preRollVideoAd != null)
            preRollVideoAd.destroyView();
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
        interstitialAd.show();
        ((TextView) findViewById(R.id.textView)).setText("Interstitial Ad winner: " + interstitialAd.getWinningPartnerName());
    }

    @Override
    public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Interstitial No-Fill");
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

    /**
     * view - The preroll video ad view
     */
    @Override
    public void onPrerollAdLoaded(View view) {

        if (bannerAd != null)
            bannerAd.destroyView();

        ((TextView) findViewById(R.id.textView)).setText("PreRoll Ad winner: " + preRollVideoAd.getWinningPartnerName());
        ((ViewGroup) findViewById(R.id.adContainer)).removeAllViews();

        if (doPrerollAdFragment) {
            showPrerollAdFragment();
        } else {
            ((ViewGroup) findViewById(R.id.adContainer)).addView(preRollVideoAd);
            preRollVideoAd.showAd();
        }
    }

    @Override
    public void onPrerollAdFailed(View view, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Preroll No-Fill");
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
        playUserContent();
    }

    private void playUserContent() {
        //Let's pretend you want to roll a movie/video when the preroll ad is completed.
        getSupportFragmentManager().popBackStack();
        videoHelper = new VideoHelper(this, findViewById(R.id.adContainer));
        videoHelper.playContentVideo(0);
    }

    @Override
    public void onRewardedVideoLoaded(LVDORewardedAd lvdoRewardedAd) {
        lvdoRewardedAd.showRewardAd("my secret code", "my userid", "V-BUCKS", "5000");
        ((TextView) findViewById(R.id.textView)).setText("Rewarded Ad winner: " + rewardedAd.getWinningPartnerName());
    }

    @Override
    public void onRewardedVideoFailed(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Rewarded No-Fill");
    }

    @Override
    public void onRewardedVideoShown(LVDORewardedAd lvdoRewardedAd) {

    }

    @Override
    public void onRewardedVideoShownError(LVDORewardedAd lvdoRewardedAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Rewarded Got Fill, but Error Showing");
    }

    @Override
    public void onRewardedVideoDismissed(LVDORewardedAd lvdoRewardedAd) {

    }

    @Override
    public void onRewardedVideoCompleted(LVDORewardedAd lvdoRewardedAd) {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CustomDialogFragment) {
            CustomDialogFragment frag = (CustomDialogFragment) fragment;
            frag.setAdView(preRollVideoAd);
        }
    }

    public static class CustomDialogFragment extends DialogFragment {

        private FrameLayout frameLayout;
        private PreRollVideoAd preRollVideoAd;

        /**
         * The system calls this to get the DialogFragment's layout, regardless
         * of whether it's being displayed as a dialog or an embedded fragment.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout to use as dialog or embedded fragment
            frameLayout = new FrameLayout(container.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            frameLayout.setBackgroundColor(Color.TRANSPARENT);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int height = (displayMetrics.widthPixels * 9) / 16;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
            params.gravity = Gravity.CENTER;
            preRollVideoAd.setLayoutParams(params);
            frameLayout.addView(preRollVideoAd);
            preRollVideoAd.showAd();
            frameLayout.setBackgroundColor(Color.BLACK);
            return frameLayout;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            ((MainActivity) getActivity()).hideSystemUI();

        }

        @Override
        public void onDetach() {
            super.onDetach();
            ((MainActivity) getActivity()).showSystemUI();
        }

        void setAdView(PreRollVideoAd preRollVideoAd) {
            this.preRollVideoAd = preRollVideoAd;
        }

        /**
         * The system calls this only when creating the layout in a dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // to modify any dialog characteristics. For example, the dialog includes a
            // title by default, but your custom layout might not need it. So here you can
            // remove the dialog title, but you must call the superclass to get the Dialog.
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i(TAG, "  onConfigurationChanged PORTRAIT");
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                //x = 1024 * 9 / 16   (1024 is the physical width)
                int height = (displayMetrics.widthPixels * 9) / 16;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
                params.gravity = Gravity.CENTER;
                preRollVideoAd.getLayoutParams().width = displayMetrics.widthPixels;
                preRollVideoAd.getLayoutParams().height = height;
            } else {
                Log.i(TAG, "  onConfigurationChanged LANDSCAPE");
                preRollVideoAd.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
                preRollVideoAd.getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
            }
        }
    }

    private void showPrerollAdFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomDialogFragment newFragment = new CustomDialogFragment();

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        }

    }

    void hideSystemUI() {

        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
              // Set the content to appear under the system bars so that the
              // content doesn't resize when the system bars hide and show.
              //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              // Hide the nav bar and status bar
              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getSupportActionBar().hide();
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    void showSystemUI() {
        getSupportActionBar().show();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() > 0) {
            return;
        }
        super.onBackPressed();
    }
}
