package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

import utils.AppRater;
import utils.ClickListener;
import utils.FireBaseHandler;
import utils.TopicListAdapter;

public class TopicActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FireBaseHandler fireBaseHandler;

    ArrayList<Object> mArraylist = new ArrayList<>();
    ListView topicAndTestListview;
    TopicListAdapter adapter;

    Toolbar toolbar;
    int check;

    ProgressDialog progressDialog;
    private LinearLayout linearLayout;

    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_navigation_topic);

        toolbar = (Toolbar) findViewById(R.id.toolbar_topic);
        setSupportActionBar(toolbar);

        fireBaseHandler = new FireBaseHandler();
        openDynamicLink();

        FirebaseMessaging.getInstance().subscribeToTopic("subscribed");

        initializeActivity();

    }

    @Override
    protected void onDestroy() {

        FireBaseHandler.removeListener();

        if (adView !=null){
            adView.destroy();
        }

        super.onDestroy();
    }


    public void initializeActivity() {

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_topiclist_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_topiclist_view);
        navigationView.setNavigationItemSelectedListener(this);

        mArraylist = new ArrayList<>();
        topicAndTestListview = (ListView) findViewById(R.id.topicActivity_topic_listview);


        //calling rate now dialog

        AppRater.app_launched(TopicActivity.this);


        //download list of Topics
        showDialog("Loading...Please wait");
        downloadTopicList();


        setListViewFooter();
        setListViewHeader();

    }

    private void setListViewHeader() {

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        topicAndTestListview.addHeaderView(linearLayout);

        final View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_textview, null, false);

        TextView topicNameTextview = (TextView) footerView.findViewById(R.id.custom_textview);
        topicNameTextview.setText("Take Test");
        topicNameTextview.setTextColor(Color.parseColor("#FFFFFF"));

        CardView cardView = (CardView) footerView.findViewById(R.id.custom_background_cardView);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRandomTestActivity(footerView);
            }
        });

        linearLayout.addView(footerView);



        // Instantiate an AdView view
        adView = new AdView(this, "1510043762404923_1510291979046768", AdSize.BANNER_HEIGHT_50);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                try {
                    Answers.getInstance().logCustom(new CustomEvent("Ad failed").putCustomAttribute("message", adError.getErrorMessage()).putCustomAttribute("Placement","banner"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        View headerAdView = ((LayoutInflater) TopicActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_textview, null, false);


        CardView cardAdView = (CardView) headerAdView.findViewById(R.id.custom_background_cardView);
        cardAdView.removeAllViews();
        cardAdView.addView(adView);

        adView.loadAd();

        linearLayout.addView(headerAdView);


    }

    private void setListViewFooter() {
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_textview, null, false);

        TextView topicNameTextview = (TextView) footerView.findViewById(R.id.custom_textview);
        topicNameTextview.setText("Logical Reasoning Master");
        topicNameTextview.setTextColor(Color.parseColor("#FFFFFF"));

        CardView cardView = (CardView) footerView.findViewById(R.id.custom_background_cardView);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogicalReasoningClick();
            }
        });
        topicAndTestListview.addFooterView(footerView);

    }

    public void itemClick(String item, int position) {
        if (check == 0) {

            openMainActivity(0, item, null);
            Toast.makeText(TopicActivity.this, "In Topic " + " Selected " + item + " Postion is " + position, Toast.LENGTH_SHORT).show();


        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_topic:
                    downloadTopicList();
                    return true;

                case R.id.navigation_test_Series:
                    downloadTestList();
                    return true;
            }
            return false;
        }

    };

    public void showDialog(String message) {
        progressDialog = new ProgressDialog(TopicActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        try {
            progressDialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadTopicList() {
        FireBaseHandler fireBaseHandler = new FireBaseHandler();

        fireBaseHandler.downloadTopicList(30, new FireBaseHandler.OnTopiclistener() {
            @Override
            public void onTopicDownLoad(String topic, boolean isSuccessful) {

            }

            @Override
            public void onTopicListDownLoad(ArrayList<String> topicList, boolean isSuccessful) {

                if (isSuccessful) {
                    //  Toast.makeText(TopicActivity.this, "size is " + topicList.size(), Toast.LENGTH_SHORT).show();


                    //mArraylist = topicList;

                    mArraylist.clear();

                    for (Object name : topicList) {
                        mArraylist.add(name);
                    }

                    check = 0;


                    adapter = new TopicListAdapter(getApplicationContext(), R.layout.custom_textview, mArraylist);

                    adapter.setOnItemCLickListener(new ClickListener() {
                        @Override
                        public void onItemCLickListener(View view, int position) {
                            TextView textview = (TextView) view;

                            if (check == 0) {

                                openMainActivity(0, textview.getText().toString(), null);
                                //  Toast.makeText(TopicActivity.this, " Selected " + textview.getText().toString(), Toast.LENGTH_SHORT).show();

                                try {
                                    Answers.getInstance().logCustom(new CustomEvent("Topic open").putCustomAttribute("topic", textview.getText().toString()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });

                    topicAndTestListview.post(new Runnable() {
                        public void run() {
                            topicAndTestListview.setAdapter(adapter);
                        }
                    });


                    hideDialog();

                }
                hideDialog();

            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

            }
        });


    }

    private void initializeTopicList() {

    }


    public void uploadTopicName() {
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.uploadTopicName("Time and Work", new FireBaseHandler.OnTopiclistener() {
            @Override
            public void onTopicDownLoad(String topic, boolean isSuccessful) {

                if (isSuccessful) {
                    Toast.makeText(TopicActivity.this, topic + " Topic Uploaded ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTopicListDownLoad(ArrayList<String> topicList, boolean isSuccessful) {

            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {


            }
        });

    }

    public void uploadTestName() {

        fireBaseHandler.uploadTestName("TCS2017 ", new FireBaseHandler.OnTestSerieslistener() {
            @Override
            public void onTestDownLoad(String test, boolean isSuccessful) {
                if (isSuccessful) {
                    //Toast.makeText(TopicActivity.this, "Test Name is " + test, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTestListDownLoad(ArrayList<String> testList, boolean isSuccessful) {

            }

            @Override
            public void onTestUpload(boolean isSuccessful) {

            }
        });
    }


    public void downloadTestList() {
        fireBaseHandler.downloadTestList(30, new FireBaseHandler.OnTestSerieslistener() {
            @Override
            public void onTestDownLoad(String test, boolean isSuccessful) {

            }

            @Override
            public void onTestListDownLoad(ArrayList<String> testList, boolean isSuccessful) {

                if (isSuccessful) {
                    // Toast.makeText(TopicActivity.this, "size is " + testList.size(), Toast.LENGTH_SHORT).show();
                    //mArraylist = testList;

                    mArraylist.clear();

                    for (Object name : testList) {
                        mArraylist.add(name);
                    }

                    check = 1;
                    adapter = new TopicListAdapter(getApplicationContext(), R.layout.custom_textview, mArraylist);

                    adapter.setOnItemCLickListener(new ClickListener() {
                        @Override
                        public void onItemCLickListener(View view, int position) {
                            TextView textview = (TextView) view;
                            if (check == 1) {
                                openMainActivity(1, textview.getText().toString(), null);
                                //  Toast.makeText(TopicActivity.this, "In Test " + " Selected " + textview.getText().toString() + " Postion is " + position, Toast.LENGTH_SHORT).show();

                                try {
                                    Answers.getInstance().logCustom(new CustomEvent("Test series open").putCustomAttribute("test name", textview.getText().toString()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });


                    topicAndTestListview.post(new Runnable() {
                        public void run() {
                            topicAndTestListview.setAdapter(adapter);
                        }
                    });


                    hideDialog();

                }
                hideDialog();

            }

            @Override
            public void onTestUpload(boolean isSuccessful) {

            }
        });
    }

    public void openMainActivity(int check, String Text, String questionUID) {

        Intent intent = new Intent(TopicActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("check", check);
        bundle.putString("Topic", Text);
        bundle.putString("questionUID", questionUID);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void openDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d("DeepLink", "onSuccess: " + deepLink);

                            String questionID = deepLink.getQueryParameter("questionID");
                            String questionTopicName = deepLink.getQueryParameter("questionTopic");

                            //Toast.makeText(MainActivity.this, "Story id " + shortStoryID, Toast.LENGTH_SHORT).show();

                            //download question
                            if (questionID != null) {
                                openMainActivity(0, questionTopicName, questionID);
                                try {
                                    Answers.getInstance().logCustom(new CustomEvent("Via dyanamic link").putCustomAttribute("Question id", questionID).putCustomAttribute("question topic", questionTopicName));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //initializeActivity();
                        Log.w("DeepLink", "getDynamicLink:onFailure", e);
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_test) {
            Intent intent = new Intent(TopicActivity.this, RandomTestActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_suggest) {

            giveSuggestion();
        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            //sharingIntent.putExtra(Intent.EXTRA_STREAM, newsMetaInfo.getNewsImageLocalPath());

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    " " + "\n\n https://goo.gl/Q7sjZi " + "\n Aptitude Quiz app \n Download App Now");
            startActivity(Intent.createChooser(sharingIntent, "Share Aptitude App via"));


        } else if (id == R.id.nav_rate) {
            onRateUs();
        } else if (id == R.id.nav_logical_reasoning) {
            onLogicalReasoningClick();
        } else if (id == R.id.nav_personality_development) {
            onPersonalityDevelopment();
        } else if (id == R.id.nav_daily_editorial) {
            onDailyEditorialClick();
        } else if ((id == R.id.nav_pib)) {
            onPIBClick();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_topiclist_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void onPIBClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.crafty.studio.current.affairs.pib";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("PIB CLick"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDailyEditorialClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.craftystudio.vocabulary.dailyeditorial";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Daily Editorial"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPersonalityDevelopment() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.story.craftystudio.shortstory";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Personality Development"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLogicalReasoningClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.reasoning.logical.quiz.craftystudio.logicalreasoning";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Logical Reasoning Click"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void giveSuggestion() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"acraftystudio@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion For " + getResources().getString(R.string.app_name));
        emailIntent.setType("text/plain");

        startActivity(Intent.createChooser(emailIntent, "Send mail From..."));

    }

    private void onRateUs() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.aptitude.quiz.craftystudio.aptitudequiz";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } catch (Exception e) {

        }
    }

    public void openRandomTestActivity(View view) {

        try {
            Answers.getInstance().logCustom(new CustomEvent("Random Test").putCustomAttribute("random test", "test"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(TopicActivity.this, RandomTestActivity.class);

        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_topiclist_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //showDialog("Loading.. Please Wait");
        //  downloadTopicList();

        //initializeActivity();
    }
}
