package app.aptitude.quiz.craftystudio.aptitudequiz;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.menu_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        viewPager = (ViewPager) findViewById(R.id.practice_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.practice_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_topic:
                    // downloadAptitudeTopicList();
                    return true;

                case R.id.navigation_daily_quiz:
                    downloadDateList();
                    return true;

                case R.id.navigation_test_Series:
                    downloadTestList();
                    return true;


            }
            return false;
        }

    };

    private void downloadTestList() {
        Intent intent = new Intent(MenuActivity.this, TopicActivity.class);
        intent.putExtra("study", 0);
        intent.putExtra("subject", 5);
        startActivity(intent);
    }

    private void downloadDateList() {
        Intent intent = new Intent(MenuActivity.this, TopicActivity.class);
        intent.putExtra("study", 0);
        intent.putExtra("subject", 6);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MenuPracticeFragment(), "PRACTICE");
        adapter.addFragment(new MenuStudyFragment(), "STUDY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_test) {

            Intent intent = new Intent(MenuActivity.this, RandomTestActivity.class);
            startActivity(intent);
            //Intent intent = new Intent(TopicActivity.this, RandomTestActivity.class);
            //startActivity(intent);

        } else if (id == R.id.nav_bookmark) {

            Intent intent = new Intent(MenuActivity.this, BookmarkActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_suggest) {

            giveSuggestion();
        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            //sharingIntent.putExtra(Intent.EXTRA_STREAM, newsMetaInfo.getNewsImageLocalPath());

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    " " + "\n\n https://goo.gl/Q7sjZi " + "\n Aptitude Quiz app \n Download App Now");
            startActivity(Intent.createChooser(sharingIntent, "Share Aptitude Master App via"));


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
        } else if ((id == R.id.nav_basic_Computer)) {
            onBasicComputerClick();
        } else if ((id == R.id.nav_short_key)) {
            onShortKeyClick();
        } else if ((id == R.id.nav_English_grammar)) {
            onEnglishGrammarClick();
        } else if ((id == R.id.nav_idioms_phrases)) {
            onIdiomsAndPhrasesClick();
        } else if ((id == R.id.nav_Current_affair)) {
            onCurrentAffairsClick();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_drawer_layout);
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

    private void onBasicComputerClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.computer.basic.quiz.craftystudio.computerbasic";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Basic Computer CLick"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onShortKeyClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.key.ashort.craftystudio.shortkeysapp";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("ShortKey CLick"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCurrentAffairsClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=gk.affairs.current.craftystudio.app.currentaffairs";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Current Affairs CLick"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onIdiomsAndPhrasesClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.craftystudio.phrases.idiom.idiomsandphrases";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("Idioms and Phrases CLick"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onEnglishGrammarClick() {
        try {
            String link = "https://play.google.com/store/apps/details?id=app.english.grammar.craftystudio.englishgrammar";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            Answers.getInstance().logCustom(new CustomEvent("English Grammar CLick"));

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

        Intent intent = new Intent(MenuActivity.this, RandomTestActivity.class);

        startActivity(intent);

    }

}
