package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.mainActivity_viewpager);

        initializeViewPager();

        check = getIntent().getExtras().getInt("check");

        if (check == 0) {

            //if check == 0 get questions by topic name
            topicName = getIntent().getExtras().getString("Topic");
            String questionUID = getIntent().getExtras().getString("questionUID");

            if (questionUID != null) {

                downloadQuestion(questionUID);
            }
            downloadQuestionByTopic(topicName);
            Toast.makeText(this, "In Topic", Toast.LENGTH_SHORT).show();

        } else if (check == 1) {

            //check==1 get question by test name
            testName = getIntent().getExtras().getString("Topic");
            downloadQuestionByTestName(testName);
            Toast.makeText(this, "In Test", Toast.LENGTH_SHORT).show();
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

        } else {

        }


    }

    /* Download questions according to TOPIC name*/
    public void downloadQuestionByTopic(String topic) {
        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadQuestionList(topic, 8, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                if (isSuccessful) {

                    for (Questions questions : questionList) {
                        mQuestionsList.add(questions);
                    }
                    initializeViewPager();

                    mPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onQuestionUpload(boolean isSuccessful) {

            }
        });

    }


    /* Download questions according to TEST name*/
    public void downloadQuestionByTestName(String testName) {
        fireBaseHandler = new FireBaseHandler();

        isMoreQuestionAvailable = false;
        fireBaseHandler.downloadQuestionList(30, testName, new FireBaseHandler.OnQuestionlistener() {
            @Override
            public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

            }

            @Override
            public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                if (isSuccessful) {

                    for (Questions questions : questionList) {
                        mQuestionsList.add(questions);
                    }
                    initializeViewPager();

                    mPagerAdapter.notifyDataSetChanged();
                }

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        if (id == R.id.nav_camera) {
            fireBaseHandler = new FireBaseHandler();
            fireBaseHandler.downloadQuestionList("Number Basic", 5, new FireBaseHandler.OnQuestionlistener() {
                @Override
                public void onQuestionDownLoad(Questions questions, boolean isSuccessful) {

                }

                @Override
                public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful) {

                    if (isSuccessful) {
                        mQuestionsList = questionList;
                        initializeViewPager();

                        mPagerAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onQuestionUpload(boolean isSuccessful) {

                }
            });
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(MainActivity.this, TopicActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(MainActivity.this, RandomTestActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

}


