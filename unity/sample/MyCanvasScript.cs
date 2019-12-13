using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using Chocolate;

public class MyCanvasScript : MonoBehaviour, ChocolateRewardCallbackReceiver, ChocolateInterstitialCallbackReceiver
{

    private const string TAG = "MyCanvasScript";

    private void Start()
    {
        log("Start");
        //ChocolateUnityBridge.SetAdRequestTestMode(true, "xxxxxxxx");  //OPTIONAL TEST MODE
        //appInfo is mandatory. However, ads can still be fetched.
        ChocolateUnityBridge.setAppInfo("MyAppName", "MyPublisherName","MyDomainName", 
                                    "PublisherDomain", "MyPlayStoreUrl", "My IAB Category");
        //OPTIONAL: Ad Request User Parameters
        //ChocolateUnityBridge.SetAdRequestUserParams(18, "", "M", "Single", "Asian", "", "", "", "", "");
        ChocolateUnityBridge.initWithAPIKey("XqjhRR");  //Our test key.  Replace with yours in production.
        ChocolateUnityBridge.setInterstitialAdListener(this);
        ChocolateUnityBridge.setRewardAdListener(this);
    }

    public void onInterstitialLoaded(string msg)
    {
        //interstitial ad is ready!  You can display now, or you can wait until a later time.
        updateStatusUI("Interstitial Ad Winner: " + ChocolateUnityBridge.GetInterstitialAdWinner());
        showInterstitialAd();
    }
    public void onInterstitialFailed(string msg)
    {
        updateStatusUI("Interstitial Ad: no-fillunRegisterAdListener");
        //no need to pre-fetch the next ad here.  this will be done internally and automatically.
    }
    public void onInterstitialShown(string msg)
    {

    }
    public void onInterstitialClicked(string msg)
    {

    }
    public void onInterstitialDismissed(string msg)
    {

    }

    public void onRewardLoaded(string msg)
    {
        updateStatusUI("Rewarded Ad Winner: " + ChocolateUnityBridge.GetRewardAdWinner());

        //reward ad is ready!  You can display now, or you can wait until a later time.
        showRewardAd();
    }
    public void onRewardFailed(string msg)
    {
        updateStatusUI("Rewarded Ad: no-fill");
        //no need to pre-fetch the next ad here.  this will be done internally and automatically.
    }
    public void onRewardShown(string msg)
    {

    }
    public void onRewardFinished(string msg)
    {
        //TODO: REWARD USER HERE
    }
    public void onRewardDismissed(string msg)
    {
        //no need to pre-fetch the next ad here.  this will be done internally and automatically.
    }
 
    //===============Interstitial Ad Methods===============

    public void loadInterstitialAd()     //called when btnLoadInterstitial Clicked
    {
        log("Load Interstitial Ad...");
        ChocolateUnityBridge.loadInterstitialAd();
    }

    private void showInterstitialAd()     //called when btnShowInterstitial Clicked
    {
        log("Show Interstitial Ad...");
        ChocolateUnityBridge.showInterstitialAd();
    }

    //===============Rewarded Video Ad Methods===============

    public void loadRewardAd()       //called when btnRequestReward Clicked
    {
        log("Request Reward Ad...");  //can take anywhere from 0 to 7 seconds
        ChocolateUnityBridge.loadRewardAd();
    }

    private void showRewardAd()           //called when btnShowReward Clicked
    {
        log("Show Reward Ad...");
        ChocolateUnityBridge.showRewardAd(30, "coins", "", "qwer1234");
    }

    private void updateStatusUI(string newStatus)
    {
        updateStatusUI("StatusText", newStatus);
    }

    private void updateStatusUI(string name, string newStatus)
    {
        try
        {
            Text textView = GameObject.Find(name).GetComponent<Text>();
            if (textView != null && textView.text != null)
            {
                textView.text = newStatus;
            }
        }catch (System.Exception e)
        {
            log("updateStatusUI failed");
        }
    }

    public void loadNewScene()
    {
        unRegisterAdListener();
        SceneManager.LoadScene("MySecondScene", LoadSceneMode.Additive);
    }

    //Be sure to unRegister before loading a new scene.
    private void unRegisterAdListener()
    {
        ChocolateUnityBridge.removeRewardAdListener();
        ChocolateUnityBridge.removeInterstitialAdListener();
    }

    private void OnDestroy()
    {
        log("OnDestroy Remove Ad event listener");
        unRegisterAdListener();
    }

    private void log(string msg)
    {
        Debug.Log(TAG + " " + msg);
    }


}
