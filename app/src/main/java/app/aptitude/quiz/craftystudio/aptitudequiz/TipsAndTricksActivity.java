package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import utils.FireBaseHandler;

public class TipsAndTricksActivity extends AppCompatActivity {

    String mSubTopicName, mMainTopicName;
    int msubjectCode;
    WebView webView;

    ProgressDialog progressDialog;
    private NativeAd nativeAd;
    private NativeAd topNativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_and_tricks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSubTopicName = getIntent().getStringExtra("SubTopicName");
        mMainTopicName = getIntent().getStringExtra("MainTopicName");
        msubjectCode = getIntent().getIntExtra("SubjectCode", 0);

        toolbar.setTitle(mSubTopicName);
        setSupportActionBar(toolbar);


        //download data from database

        if (mSubTopicName.equalsIgnoreCase("H.C.F and L.C.M"))
            mSubTopicName = "HCF and LCM";


        switch (msubjectCode) {
            case 0:
                downloadAptitudeData(mSubTopicName);
                break;
            case 1:
                downloadEnglishGrammarData(mMainTopicName, mSubTopicName);
                break;
            case 2:
                downloadComputerData(mMainTopicName, mSubTopicName);
                break;
        }


        try {
            Answers.getInstance().logCustom(new CustomEvent("Tips and Tricks").putCustomAttribute("Topic", mSubTopicName));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            initializeBottomNativeAds();
            initializeTopNativeAds();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void downloadAptitudeData(String mTopicName) {
        showDialog();
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadAptitudeTipsAndTricks(mTopicName, new FireBaseHandler.OnTipsAndTrickslistener() {
            @Override
            public void onTipsDownLoad(String tips, boolean isSuccessful) {
                hideDialog();

                if (isSuccessful) {
                    if (tips != null) {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", tips, "text/html", "UTF-8", "");

                    } else {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", "Data Cannot be loaded", "text/html", "UTF-8", "");

                    }
                }
            }
        });

    }

    private void downloadComputerData(final String mMainTopicName, final String mSubTopicName) {
        showDialog();
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadComputerStudyData(mMainTopicName, mSubTopicName, new FireBaseHandler.OnTipsAndTrickslistener() {
            @Override
            public void onTipsDownLoad(String computerStudyData, boolean isSuccessful) {
                hideDialog();
                if (isSuccessful) {
                    if (computerStudyData != null) {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", computerStudyData, "text/html", "UTF-8", "");

                    } else {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", "Data not Available", "text/html", "UTF-8", "");

                    }
                }

            }
        });

    }

    private void downloadEnglishGrammarData(String mMainTopicName, String mSubTopicName) {
        showDialog();
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadEnglishGrammarStudyData(mMainTopicName, mSubTopicName, new FireBaseHandler.OnTipsAndTrickslistener() {
            @Override
            public void onTipsDownLoad(String englishGrammarStudyData, boolean isSuccessful) {
                hideDialog();
                if (isSuccessful) {
                    if (englishGrammarStudyData != null) {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", englishGrammarStudyData, "text/html", "UTF-8", "");

                    } else {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", "Data not Available", "text/html", "UTF-8", "");

                    }
                }

            }
        });
    }

    void showDialog() {
        progressDialog = new ProgressDialog(TipsAndTricksActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    void hideDialog() {
        progressDialog.hide();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void initializeBottomNativeAds() {

        if (nativeAd == null) {

            nativeAd = new NativeAd(this, "1510043762404923_1526508060758493");
            nativeAd.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {

                    try {
                        initializeAdmob();
                        Answers.getInstance().logCustom(new CustomEvent("Ad failed").putCustomAttribute("Placement", "Tips and trick").putCustomAttribute("error", adError.getErrorMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdLoaded(Ad ad) {


                    View adView = NativeAdView.render(TipsAndTricksActivity.this, nativeAd, NativeAdView.Type.HEIGHT_400);
                    CardView nativeAdContainer = (CardView) findViewById(R.id.tips_adContainer_LinearLayout);
                    // Add the Native Ad View to your ad container
                    nativeAdContainer.addView(adView);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            // Initiate a request to load an ad.
            nativeAd.loadAd();
        }


    }


    private void initializeAdmob() {

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId("ca-app-pub-8455191357100024/3421218302");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        CardView nativeAdContainer = findViewById(R.id.tips_adContainer_LinearLayout);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);


    }

    public void initializeTopNativeAds() {

        if (topNativeAd == null) {

            topNativeAd = new NativeAd(this, "1510043762404923_1526511874091445");
            topNativeAd.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {


                    try {
                        initializeTopAdmob();
                        Answers.getInstance().logCustom(new CustomEvent("Ad failed").putCustomAttribute("Placement", "Tips and trick").putCustomAttribute("error", adError.getErrorMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdLoaded(Ad ad) {


                    View adView = NativeAdView.render(TipsAndTricksActivity.this, topNativeAd, NativeAdView.Type.HEIGHT_120);
                    CardView nativeAdContainer = (CardView) findViewById(R.id.tips_topadContainer_LinearLayout);
                    // Add the Native Ad View to your ad container
                    nativeAdContainer.addView(adView);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            // Initiate a request to load an ad.
            topNativeAd.loadAd();
        }


    }

    private void initializeTopAdmob() {

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId("ca-app-pub-8455191357100024/3421218302");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        CardView nativeAdContainer = findViewById(R.id.tips_topadContainer_LinearLayout);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
    }


    @Override
    protected void onDestroy() {

        FireBaseHandler.removeListener();

        super.onDestroy();
    }


}
