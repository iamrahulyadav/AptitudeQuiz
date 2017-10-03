package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.ArrayList;

import utils.AppRater;
import utils.FireBaseHandler;

public class TopicActivity extends AppCompatActivity {

    private TextView mTextMessage;
    FireBaseHandler fireBaseHandler;

    ArrayList<String> mArraylist;
    ListView topicAndTestListview;
    ArrayAdapter adapter;

    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


        fireBaseHandler = new FireBaseHandler();


        openDynamicLink();
    }


    public void initializeActivity() {

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mArraylist = new ArrayList<>();


        topicAndTestListview = (ListView) findViewById(R.id.topicActivity_topic_listview);

        topicAndTestListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textview = (TextView) view;

                if (check == 0) {

                    openMainActivity(0, textview.getText().toString(), null);
                    Toast.makeText(TopicActivity.this, "In Topic " + " Selected " + textview.getText().toString() + " Postion is " + i, Toast.LENGTH_SHORT).show();


                } else if (check == 1) {
                    openMainActivity(1, textview.getText().toString(), null);
                    Toast.makeText(TopicActivity.this, "In Test " + " Selected " + textview.getText().toString() + " Postion is " + i, Toast.LENGTH_SHORT).show();

                }

            }
        });


        //calling rate now dialog
        AppRater appRater = new AppRater();
        appRater.app_launched(TopicActivity.this);


        //download list of Topics
        downloadTopicList();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    downloadTopicList();
                    return true;

                case R.id.navigation_notifications:
                    downloadTestList();
                    return true;
            }
            return false;
        }

    };

    private void downloadTopicList() {
        FireBaseHandler fireBaseHandler = new FireBaseHandler();

        fireBaseHandler.downloadTopicList(20, new FireBaseHandler.OnTopiclistener() {
            @Override
            public void onTopicDownLoad(String topic, boolean isSuccessful) {

            }

            @Override
            public void onTopicListDownLoad(ArrayList<String> topicList, boolean isSuccessful) {

                if (isSuccessful) {
                    Toast.makeText(TopicActivity.this, "size is " + topicList.size(), Toast.LENGTH_SHORT).show();

                    mArraylist = topicList;

                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview, mArraylist);

                    topicAndTestListview.setAdapter(adapter);

                    check = 0;

                }
            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

            }
        });

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
                    Toast.makeText(TopicActivity.this, "Test Name is " + test, Toast.LENGTH_SHORT).show();
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
        fireBaseHandler.downloadTestList(20, new FireBaseHandler.OnTestSerieslistener() {
            @Override
            public void onTestDownLoad(String test, boolean isSuccessful) {

            }

            @Override
            public void onTestListDownLoad(ArrayList<String> testList, boolean isSuccessful) {

                if (isSuccessful) {
                    Toast.makeText(TopicActivity.this, "size is " + testList.size(), Toast.LENGTH_SHORT).show();
                    mArraylist = testList;

                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview, mArraylist);
                    topicAndTestListview.setAdapter(adapter);

                    check = 1;
                }
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
                                    // Answers.getInstance().logCustom(new CustomEvent("Via dyanamic link").putCustomAttribute("Story id", questionID));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            // downloadNewsArticle(newsArticleID);

                        } else {
                            Log.d("DeepLink", "onSuccess: ");

                            //download story list


                            try {
                                Intent intent = getIntent();
                                String questionID = intent.getStringExtra("questionID");
                                String questionTopicName = intent.getStringExtra("questionTopic");
                                if (questionID == null) {
                                    initializeActivity();
                                } else {
                                    //download story
                                    // openMainActivity(0, questionTopicName, questionID);
                                    try {
                                        // Answers.getInstance().logCustom(new CustomEvent("Via push notification").putCustomAttribute("Story id", storyID));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //   Toast.makeText(this, "Story id is = "+storyID, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                initializeActivity();
                                e.printStackTrace();
                            }


                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        initializeActivity();
                        Log.w("DeepLink", "getDynamicLink:onFailure", e);
                    }
                });
    }

}
