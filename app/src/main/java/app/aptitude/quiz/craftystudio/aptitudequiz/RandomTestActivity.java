package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import utils.FireBaseHandler;
import utils.Questions;
import utils.ZoomOutPageTransformer;

public class RandomTestActivity extends AppCompatActivity {


    FireBaseHandler fireBaseHandler;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<Questions> mQuestionsList = new ArrayList<>();

    static boolean isRandomTestQuestions = false;

    TextView testTextview;


    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_test);
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


        mPager = (ViewPager) findViewById(R.id.random_test_activity_viewpager);

        downloadRandomQuestionList();
        initializeViewPager();

        Button submitTest = (Button) findViewById(R.id.fragmentAptitudeQuiz_Submit_Button);

        submitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculateResult();
                isRandomTestQuestions = false;

                initializeViewPager();

                countDownTimer.cancel();
            }
        });

        testTextview = (TextView) findViewById(R.id.randomactivity_countdown_timer);


    }

    private void calculateResult() {
        int rightAnswer = 0, wrongAnswer = 0;
        for (Questions question : mQuestionsList) {
            if (question.getCorrectAnswer().equalsIgnoreCase(question.getUserAnswer())) {
                rightAnswer++;
            }
        }
        wrongAnswer = 10 - rightAnswer;

        Toast.makeText(this, "right answers " + rightAnswer + " wrong answers " + wrongAnswer, Toast.LENGTH_SHORT).show();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void downloadRandomQuestionList() {

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
                            testTextview.setText( minutes + ":" + seconds);
                        }

                        public void onFinish() {
                            testTextview.setText("Time up!");
                            calculateResult();
                            isRandomTestQuestions = false;

                            initializeViewPager();
                        }
                    }.start();


                } else {
                    //  openConnectionFailureDialog();
                }
            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }

        });
    }


}
