package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.Collections;

import utils.DataBaseHandler;
import utils.Questions;
import utils.ZoomOutPageTransformer;

public class BookmarkActivity extends AppCompatActivity {


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<Questions> mQuestionsList = new ArrayList<>();

    ProgressDialog progressDialog;

    Questions questions = new Questions();

    TextView questionTopicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
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


        mPager = (ViewPager) findViewById(R.id.bookmarkActivity_viewpager);
        //Setting Question TopicName
        questionTopicName = (TextView) findViewById(R.id.bookmark_topicName_Textview);

        downloadBookmarkQuestion();
        initializeViewPager();

        if (mQuestionsList.size() != 0) {
            questionTopicName.setText(mQuestionsList.get(mPager.getCurrentItem()).getQuestionTopicName());
        }else {
            questionTopicName.setText("No Bookmark");
        }


    }

    private void downloadBookmarkQuestion() {
        DataBaseHandler db = new DataBaseHandler(BookmarkActivity.this);

        // Reading all bookmarked Question
        ArrayList<Questions> bookmark_questions = db.getAllBookmarkedQuestion();
        for (Questions questions : bookmark_questions) {
            mQuestionsList.add(questions);
        }

        Collections.reverse(mQuestionsList);
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


                questionTopicName.setText(mQuestionsList.get(position).getQuestionTopicName());

                //checkInterstitialAds();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void onDeleteBookmark() {


        DataBaseHandler db = new DataBaseHandler(BookmarkActivity.this);
        db.deleteBookmarkQuestion(mQuestionsList.get(mPager.getCurrentItem()));
        mQuestionsList.remove(mQuestionsList.get(mPager.getCurrentItem()));

        initializeViewPager();
        questionTopicName.setText(mQuestionsList.get(mPager.getCurrentItem()).getQuestionTopicName());


    }


    public void openDeleteBookmarkDialog(View view) {

        if (mQuestionsList.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
            builder.setTitle("Delete!");
            builder.setMessage("Are you sure you want to delete this Question from Bookmark ? ")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            onDeleteBookmark();
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();

        } else {
            Toast.makeText(this, "No Bookmark Added!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            //  adsCount++;

            return AptitudeFragment.newInstance(mQuestionsList.get(position), BookmarkActivity.this, false);
        }

        @Override
        public int getCount() {
            return mQuestionsList.size();
        }

    }

    public void onShareQuestion(View view) {
        if (mQuestionsList.size() != 0) {
            questions = mQuestionsList.get(mPager.getCurrentItem());
            onShareClick();
        } else {
            Toast.makeText(this, "No Bookmark Added!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(String message) {
        progressDialog = new ProgressDialog(BookmarkActivity.this);
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


    public void openTipsAndTricks(View view) {

        if (mQuestionsList.size() != 0) {

            questions = mQuestionsList.get(mPager.getCurrentItem());

            Intent intent = new Intent(BookmarkActivity.this, TipsAndTricksActivity.class);
            intent.putExtra("TopicName", questions.getQuestionTopicName());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Bookmark Added!", Toast.LENGTH_SHORT).show();
        }
    }

}
