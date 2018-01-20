package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.TopicListAdapter;

public class TipsTopicListActivity extends AppCompatActivity {


    ArrayList<Object> mArraylist;
    ListView topicAndTestListview;
    TopicListAdapter adapter;
    int check;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_topic_list);
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


        mArraylist = new ArrayList<>();
        topicAndTestListview = (ListView) findViewById(R.id.tipsTopicListActivity_topic_listview);


        //download list of Topics
        showDialog("Loading...Please wait");
        downloadTopicList();

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDialog(String message) {
        progressDialog = new ProgressDialog(TipsTopicListActivity.this);
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

                    mArraylist.clear();

                    for (Object name : topicList) {
                        mArraylist.add(name);
                    }


                    adapter = new TopicListAdapter(getApplicationContext(), R.layout.custom_textview, mArraylist);

                    adapter.setOnItemCLickListener(new ClickListener() {
                        @Override
                        public void onItemCLickListener(View view, int position) {
                            TextView textview = (TextView) view;


                            //opening TipsAndTricks Activity
                            openTipsAndTrickActivty(textview.getText().toString());

                            try {
                                Answers.getInstance().logCustom(new CustomEvent("Tips List open").putCustomAttribute("topic", textview.getText().toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });

                    topicAndTestListview.post(new Runnable() {
                        public void run() {
                            topicAndTestListview.setAdapter(adapter);
                        }
                    });




                }
                hideDialog();

            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

            }
        });


    }

    private void openTipsAndTrickActivty(String topicName) {

        Intent intent = new Intent(TipsTopicListActivity.this, TipsAndTricksActivity.class);
        intent.putExtra("TopicName", topicName);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {

        FireBaseHandler.removeListener();

        super.onDestroy();
    }


}
