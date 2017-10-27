package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.w3c.dom.Text;

import java.util.ArrayList;

import utils.FireBaseHandler;
import utils.Questions;
import utils.ZoomOutPageTransformer;

public class RandomTestActivity extends AppCompatActivity implements View.OnClickListener {


    FireBaseHandler fireBaseHandler;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<Questions> mQuestionsList = new ArrayList<>();

    static boolean isRandomTestQuestions = false;

    static boolean isTestActivity = false;

    CountDownTimer countDownTimer;


    ProgressDialog progressDialog;

    static BottomSheetBehavior behavior;
    TextView mExplainationBottomsheetTextview;


    TextView mCountDownTimer, displayRightAnswers;
    ImageView mSubmitTest, mShareQuestion, mExplainQuestion, mRefreshTest;
    Questions questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabSubmitTest = (FloatingActionButton) findViewById(R.id.fab_submit_test);
        fabSubmitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //Bottombar Navigation for refresh,submit,explaination amd share
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.randomactivity_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottombar_test_refresh:
                        
                        onRefreshTest();
                        break;
                    case R.id.bottombar_test_explaination:
                        showExplaination(RandomTestActivity.this);
                        break;
                    case R.id.bottombar_test_share:
                        onShareQuestion();
                        break;
                    case R.id.bottombar_test_submit:
                        onSubmitTest();
                        break;
                }
                return true;
            }
        });


        mPager = (ViewPager) findViewById(R.id.random_test_activity_viewpager);
        mCountDownTimer = (TextView) findViewById(R.id.randomactivity_countdown_timer);
        displayRightAnswers = (TextView) findViewById(R.id.randomactivity_right_WRONG_answersDIsplay_textview);


        //Options at bottom
        mRefreshTest = (ImageView) findViewById(R.id.randomactivity_Refresh_Question_imageview);
        mSubmitTest = (ImageView) findViewById(R.id.randomactivity_submit_test_imageview);
        mShareQuestion = (ImageView) findViewById(R.id.randomactivity_share_question_imageview);
        mExplainQuestion = (ImageView) findViewById(R.id.randomactivity_explaination_imageview);

        mRefreshTest.setOnClickListener(this);
        mSubmitTest.setOnClickListener(this);
        mShareQuestion.setOnClickListener(this);
        mExplainQuestion.setOnClickListener(this);


        //showing EXPLAINATION OF QUESTION USING BOTTOMSHEET
        View bottomSheet = findViewById(R.id.randomTestactivity_bottomsheet_linearlayout);
        mExplainationBottomsheetTextview = (TextView) findViewById(R.id.randomTestactivity_bottomsheet_textview);

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
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        downloadRandomQuestionList();
        initializeViewPager();

    }

    private void putExplaination() {

        Questions question = mQuestionsList.get(mPager.getCurrentItem());
        mExplainationBottomsheetTextview.setText(question.getQuestionExplaination());
    }

    public static void showExplaination(Context context) {

        if (!isRandomTestQuestions) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            Toast.makeText(context, "Complete the Test First", Toast.LENGTH_SHORT).show();
        }
    }

    private void onRefreshTest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RandomTestActivity.this);

        builder.setMessage("\n Do you Want to Start a New Test? ")
                .setTitle("Refresh");

        builder.setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                countDownTimer.cancel();
                mQuestionsList.clear();
                downloadRandomQuestionList();
                initializeViewPager();
                // displayRightAnswers.setVisibility(View.GONE);

            }
        });

        builder.setNegativeButton("Not Yet!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog mydialog = builder.create();
        mydialog.show();


    }

    private void calculateResult() {


        mCountDownTimer.setText("00:00");
        int rightAnswer = 0, wrongAnswer = 0;
        for (Questions question : mQuestionsList) {
            if (question.getCorrectAnswer().equalsIgnoreCase(question.getUserAnswer())) {
                rightAnswer++;
            }
        }


        wrongAnswer = 10 - rightAnswer;

        AlertDialog.Builder builder = new AlertDialog.Builder(RandomTestActivity.this);

        builder.setMessage("Right Answers = " + rightAnswer + " \n\n Wrong Answers = " + wrongAnswer)
                .setTitle("Results");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
        final AlertDialog mydialog = builder.create();
        //   mydialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16);
        mydialog.show();
        // displayRightAnswers.setText("Right = " + rightAnswer + " Wrong = " + wrongAnswer);
        // Toast.makeText(this, "right answers " + rightAnswer + " wrong answers " + wrongAnswer, Toast.LENGTH_SHORT).show();
    }

    private void initializeViewPager() {

        // Instantiate a ViewPager and a PagerAdapter.

        mPagerAdapter = new RandomTestActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
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

    public void onSubmitTest() {

        calculateResult();

        // displayRightAnswers.setVisibility(View.VISIBLE);
        isRandomTestQuestions = false;

        isTestActivity = true;
        initializeViewPager();

        countDownTimer.cancel();


    }

    public void onShareQuestion() {
        questions = mQuestionsList.get(mPager.getCurrentItem());
        onShareClick();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.randomactivity_Refresh_Question_imageview:
                onRefreshTest();
                break;
            case R.id.randomactivity_submit_test_imageview:
                onSubmitTest();
                break;
            case R.id.randomactivity_explaination_imageview:
                showExplaination(RandomTestActivity.this);
                break;
            case R.id.randomactivity_share_question_imageview:
                onShareQuestion();
                break;


        }

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //  adsCount++;
            /*
            //getting more stories
            if (position == mQuestionsList.size() - 2) {

                if (isMoreQuestionAvailable) {
                    downloadMoreQuestionList();

                }
            }
            */
            return AptitudeFragment.newInstance(mQuestionsList.get(position), RandomTestActivity.this, isRandomTestQuestions);
        }

        @Override
        public int getCount() {
            return mQuestionsList.size();
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    public void downloadRandomQuestionList() {

        showDialog();
        isRandomTestQuestions = true;
        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadQuestionList(10, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {
                if (isSuccessful) {

                    for (Questions question : questionList) {
                        RandomTestActivity.this.mQuestionsList.add(question);
                    }

                    // initializeNativeAds();
                    mPagerAdapter.notifyDataSetChanged();


                    //countdown timer for random test
                    countDownTimer = new CountDownTimer(1200000, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {
                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            mCountDownTimer.setText(minutes + ":" + seconds);
                        }

                        public void onFinish() {
                            mCountDownTimer.setText("Time up!");
                            calculateResult();
                            isRandomTestQuestions = false;

                            initializeViewPager();
                        }
                    }.start();


                } else {
                    //  openConnectionFailureDialog();
                }
                hideDialog();
            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }

        });
    }


    public void showDialog() {
        progressDialog = new ProgressDialog(RandomTestActivity.this);
        progressDialog.setMessage("Please wait..Creating link");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        progressDialog.cancel();
    }


    private void onShareClick() {
        showDialog();
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

    }


}
