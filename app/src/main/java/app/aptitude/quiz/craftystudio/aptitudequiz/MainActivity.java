package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.rm.freedrawview.FreeDrawView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import utils.FireBaseHandler;
import utils.Questions;
import utils.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FireBaseHandler fireBaseHandler;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<Questions> mQuestionsList = new ArrayList<>();

    int check;
    String topicName, testName;

    boolean isMoreQuestionAvailable = true;

    static BottomSheetBehavior behavior;
    TextView mExplainationBottomsheetTextview;


    ProgressDialog progressDialog;

    Questions questions;
    String questionUID;

    boolean isPushNotification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                uploadQuestion();

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.mainactivity_navigation_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombar_main_explaination:
                                showExplaination();
                                break;
                            case R.id.bottombar_main_share:
                                questions = mQuestionsList.get(mPager.getCurrentItem());
                                onShareClick();
                                break;

                        }
                        return true;
                    }
                });


        //showing EXPLAINATION OF QUESTION USING BOTTOMSHEET
        View bottomSheet = findViewById(R.id.mainactivity_bottomsheet_linearlayout);
        mExplainationBottomsheetTextview = (TextView) findViewById(R.id.mainactivity_bottomsheet_textview);


        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        putExplaination();

                        onClearNotePadClick(mExplainationBottomsheetTextview);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        */

        mPager = (ViewPager) findViewById(R.id.mainActivity_viewpager);

        initializeViewPager();
        check = getIntent().getExtras().getInt("check");
        TextView questionTopicName = (TextView) findViewById(R.id.fragmentAptitudeQuiz_topicName_Textview);

        if (check == 0) {

            //if check == 0 get questions by topic name
            topicName = getIntent().getExtras().getString("Topic");
            questionUID = getIntent().getExtras().getString("questionUID");

            if (questionUID != null) {

                // showDialog("Loading...Please Wait");
                downloadQuestion(questionUID);
            }
            showDialog("Loading...Please Wait");
            downloadQuestionByTopic(topicName);

            questionTopicName = (TextView) findViewById(R.id.fragmentAptitudeQuiz_topicName_Textview);
            questionTopicName.setText(topicName);

        } else if (check == 1) {

            //check==1 get question by test name
            testName = getIntent().getExtras().getString("Topic");

            showDialog("Loading...Please Wait");
            downloadQuestionByTestName(testName);
            questionTopicName.setText(testName);

        }


        questionTopicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });


        isPushNotification = getIntent().getBooleanExtra("pushNotification", false);


    }


    @Override
    protected void onDestroy() {

        FireBaseHandler.removeListener();

        super.onDestroy();
    }

    private void putExplaination() {

        Questions question = mQuestionsList.get(mPager.getCurrentItem());
        mExplainationBottomsheetTextview.setText(question.getQuestionName());
    }

    public static void showExplaination() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            Answers.getInstance().logCustom(new CustomEvent("Freenotepad").putCustomAttribute("explain", 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadQuestion(String questionUID) {

        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadQuestion(questionUID, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {
                if (isSuccessful) {
                    mQuestionsList.add(0, questions);
                    initializeViewPager();
                    mPagerAdapter.notifyDataSetChanged();

                    hideDialog();

                }


            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }

        });

    }

    public void downloadMoreRandomQuestionList() {
        fireBaseHandler.downloadQuestionList(5, mQuestionsList.get(mQuestionsList.size() - 1).getRandomNumber() + 1, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {
                if (isSuccessful) {

                    for (Questions question : questionList) {
                        MainActivity.this.mQuestionsList.add(question);
                    }

                    //initializeNativeAds();
                    mPagerAdapter.notifyDataSetChanged();

                } else {
                    // openConnectionFailureDialog();
                }
            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }

        });

    }


    public void downloadMoreQuestionList() {

        if (check == 0) {
            fireBaseHandler.downloadMoreQuestionsList(topicName, 5, mQuestionsList.get(mQuestionsList.size() - 1).getQuestionUID(), new FireBaseHandler.OnQuestionlistener() {
                @Override
                public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

                }

                @Override
                public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {
                    if (isSuccessful) {

                        for (Questions question : questionList) {

                            if (topicName.equalsIgnoreCase(question.getQuestionTopicName())) {
                                MainActivity.this.mQuestionsList.add(question);

                            } else {
                                isMoreQuestionAvailable = false;
                            }
                        }
                        addNativeAds();
                        mPagerAdapter.notifyDataSetChanged();
                        hideDialog();


                    } else {
                        hideDialog();
                        // openConnectionFailureDialog();
                    }
                }

                @Override
                public void onQuestionUpload(boolean isSuccessful) {

                }
            });

        } else {

        }
    }

    /* Download questions according to TOPIC name*/
    public void downloadQuestionByTopic(String topic) {

        initializeViewPager();

        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadQuestionList(topic, 8, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

                hideDialog();
            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                if (isSuccessful) {

                    if (mQuestionsList.size() < 5) {
                        for (Questions questions : questionList) {
                            mQuestionsList.add(questions);
                        }
                    } else {

                        addQuestionToList(questionList);

                    }

                    addNativeAds();
                    //Toast.makeText(MainActivity.this, "Question added", Toast.LENGTH_SHORT).show();
                    mPagerAdapter.notifyDataSetChanged();


                }

                hideDialog();


            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }
        });


    }

    private void addQuestionToList(ArrayList<Questions> questionList) {

        boolean add = true;

        for (int i = 0; i < mQuestionsList.size(); i++) {

            Questions questions = mQuestionsList.get(i);
            if (questions.getQuestionUID().equalsIgnoreCase(questionList.get(0).getQuestionUID())) {
                add = false;
                break;
            }

        }
        if (add) {
            mQuestionsList.add(questionList.get(0));
        }

    }


    /* Download questions according to TEST name*/
    public void downloadQuestionByTestName(String testName) {
        fireBaseHandler = new FireBaseHandler();

        isMoreQuestionAvailable = false;
        fireBaseHandler.downloadQuestionList(30, testName, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

                hideDialog();
            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                if (isSuccessful) {

                    mQuestionsList.clear();

                    for (Questions questions : questionList) {
                        mQuestionsList.add(questions);
                    }
                    initializeViewPager();

                    addNativeAds();

                    mPagerAdapter.notifyDataSetChanged();


                }

                hideDialog();

            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {


            }
        });


    }


    private void initializeViewPager() {

// Instantiate a ViewPager and a PagerAdapter.

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        //change to zoom
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //checkInterstitialAds();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void onShareQuestion(View view) {
        questions = mQuestionsList.get(mPager.getCurrentItem());
        onShareClick();
    }

    public void onShowNotePad(View view) {
        showExplaination();
    }

    public void onCloseNotePadClick(View view) {
        if (behavior != null) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void onClearNotePadClick(View view) {

        FreeDrawView freeDrawView = (FreeDrawView) findViewById(R.id.mainactivity_bottomsheet_drawView);
        freeDrawView.clearDraw();

    }

    public void openTipsAndTricks(View view) {
        questions = mQuestionsList.get(mPager.getCurrentItem());

        Intent intent = new Intent(MainActivity.this, TipsAndTricksActivity.class);
        intent.putExtra("TopicName", questions.getQuestionTopicName());
        startActivity(intent);

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //  adsCount++;
            //getting more stories
            if (position == mQuestionsList.size() - 2) {

                if (isMoreQuestionAvailable) {
                    downloadMoreQuestionList();

                }
            }

            return AptitudeFragment.newInstance(mQuestionsList.get(position), MainActivity.this, false);
        }

        @Override
        public int getCount() {
            return mQuestionsList.size();
        }

    }

    public void addNativeAds() {

        for (int i = 1; i < mQuestionsList.size(); i = i + 3) {
            if (mQuestionsList.get(i).getNativeAd() == null) {

                NativeAd nativeAd = new NativeAd(this, "1510043762404923_1510047485737884");
                nativeAd.setAdListener(new AdListener() {

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.d("TAG", "onError: " + adError.getErrorMessage());

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

                // Initiate a request to load an ad.
                nativeAd.loadAd();

                mQuestionsList.get(i).setNativeAd(nativeAd);

            }
        }
    }

    @Override
    public void onBackPressed() {

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {


            if (isPushNotification) {
                Intent intent = new Intent(this, TopicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                super.onBackPressed();
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        return true;
    }

    public void uploadQuestion() {
        Questions questions = new Questions();
        questions.setQuestionName("What least number must be subtracted from 12702 to get number exactly 99 ?");
        questions.setCorrectAnswer("30");
        questions.setOptionA("21");
        questions.setOptionB("30");
        questions.setOptionC("25");
        questions.setOptionD("29");
        questions.setPreviousYearsName("TCS 2015 &2017");
        questions.setQuestionTopicName("Number Basic");
        questions.setQuestionTestName("TCS 2015");
        questions.setQuestionExplaination("Divide the given number by 99 and find the remainder.  If you subtract the remainder from the given number then it is exactly divisible by 99.\n" +
                "99)  12702 (128\n" +
                "         99  \n" +
                "         280\n" +
                "         198\n" +
                "           822\n" +
                "           792\n" +
                "             30\n" +
                "Required number is 30.");

        //random no generate
        final int min = 1;
        final int max = 1000;
        Random random = new Random();
        final int r = random.nextInt((max - min) + 1) + min;

        questions.setRandomNumber(r);

        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.uploadQuestion(questions, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

                if (isSuccessful) {
                    Toast.makeText(MainActivity.this, "Question Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showDialog(String message) {
        progressDialog = new ProgressDialog(MainActivity.this);
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


    private void onShareClick() {
        showDialog("Creating Link...Please Wait");
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://goo.gl/Q7sjZi?questionID=" + questions.getQuestionUID() + "&questionTopic=" + questions.getQuestionTopicName()))
                .setDynamicLinkDomain("a9adz.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("app.aptitude.quiz.craftystudio.aptitudequiz")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(questions.getQuestionName())
                                .setDescription(questions.getQuestionTopicName())
                                .build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("share")
                                .setMedium("social")
                                .setCampaign("example-promo")
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();

                            openShareDialog(shortLink);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    private void openShareDialog(Uri shortUrl) {

        try {
            /*
            Answers.getInstance().logCustom(new CustomEvent("Share link created").putCustomAttribute("Content Id", questions.getQuestionUID())
                    .putCustomAttribute("Shares", questions.getQuestionTopicName()));
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        //sharingIntent.putExtra(Intent.EXTRA_STREAM, newsMetaInfo.getNewsImageLocalPath());

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "\nCan you Solve this question? \n\n " + questions.getQuestionName() + "\n\n" + "1. " + questions.getOptionA()
                        + "\n2. " + questions.getOptionB() + "\n3. " + questions.getOptionC() + "\n4. " + questions.getOptionD() + "\n\n See the Explaination here\n " + shortUrl);
        startActivity(Intent.createChooser(sharingIntent, "Share Aptitude Question via"));
        hideDialog();

        try {
            Answers.getInstance().logCustom(new CustomEvent("Share question").putCustomAttribute("question", questions.getQuestionName()).putCustomAttribute("question topic", questions.getQuestionTopicName()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}


