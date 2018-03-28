package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.TopicListAdapter;

public class StudyDataActivity extends AppCompatActivity {

    TabLayout tabLayout;
    int subjectCode = 0;

    String mMainTopicName;
    //0= aptitude, 1= english Grammar, 2= Computer Knowledge

    ArrayList<Object> mArraylist = new ArrayList<>();
    ListView topicListview;
    TopicListAdapter adapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_data);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        topicListview = (ListView) findViewById(R.id.studyDataActivity_topic_listview);

        tabLayout = (TabLayout) findViewById(R.id.study_data_tabs);
        subjectCode = getIntent().getIntExtra("subject", 0);

        switch (subjectCode) {
            case 0:
                openAptitudeStudyData();
                break;
            case 1:
                openEnglishGrammarData();
                break;
            case 2:
                openComputerKnowledgeData();
                break;
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Basic Computer")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.computer_sub_basic_computer_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Ms Excel")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.computer_sub_msexcel_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Ms PowerPoint")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.computer_sub_mspp_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Noun")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_noun_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Adjective")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_adjective_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Punctuation")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_punctuation_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Speech")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_speech_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Adverb")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_adverb_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Conjuction")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_conjuction_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Determiner")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_determiner_list)));
                    displaySubTopicList(mArraylist);
                } else if (tab.getText().equals("Prepositions")) {
                    mArraylist.clear();
                    mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_preposition_list)));
                    displaySubTopicList(mArraylist);
                }


                mMainTopicName = tab.getText().toString();
                mMainTopicName = mMainTopicName.replace(" ", "");


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void openAptitudeStudyData() {
        toolbar.setTitle("Aptitude Study Data");
        setSupportActionBar(toolbar);
        tabLayout.setVisibility(View.GONE);
        downloadAptitudeTopicList();
    }

    private void openEnglishGrammarData() {
        toolbar.setTitle("English Grammar Study Data");
        setSupportActionBar(toolbar);

        tabLayout.setVisibility(View.VISIBLE);
        List<String> englishmainTopicList = Arrays.asList(getResources().getStringArray(R.array.englishgrammar_main_topic_list));
        addTab(englishmainTopicList);
        mMainTopicName = englishmainTopicList.get(0).toString();

        mArraylist.clear();
        mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.englishgrammar_sub_noun_list)));
        displaySubTopicList(mArraylist);
    }

    private void openComputerKnowledgeData() {
        toolbar.setTitle("Computer Knowledge Study Data");
        setSupportActionBar(toolbar);

        tabLayout.setVisibility(View.VISIBLE);
        List<String> mainTopicList = Arrays.asList(getResources().getStringArray(R.array.computer_maindisplay_topic_list));
        addTab(mainTopicList);
        mMainTopicName = mainTopicList.get(0).toString();
        mMainTopicName = mMainTopicName.replace(" ", "");

        mArraylist.clear();
        mArraylist.addAll(Arrays.asList(getResources().getStringArray(R.array.computer_sub_basic_computer_list)));
        displaySubTopicList(mArraylist);
    }

    private void addTab(List<String> mainTopicList) {

        for (int i = 0; i < mainTopicList.size(); i++) {

            if (i == 0) {
                tabLayout.addTab(tabLayout.newTab().setText(mainTopicList.get(i)), i, true);

            } else {
                tabLayout.addTab(tabLayout.newTab().setText(mainTopicList.get(i)), i, false);
            }
        }

    }

    private void displaySubTopicList(final ArrayList<Object> marraylist) {

        adapter = new TopicListAdapter(getApplicationContext(), R.layout.custom_textview, marraylist);
        topicListview.setAdapter(adapter);
        adapter.setOnItemCLickListener(new ClickListener() {
            @Override
            public void onItemCLickListener(View view, int position) {
                openTipsAndTrickActivty(mMainTopicName, marraylist.get(position).toString(), subjectCode);
                //   Toast.makeText(StudyDataActivity.this, mMainTopicName + marraylist.get(position).toString() + subjectCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void downloadAptitudeTopicList() {
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
                            String topic = (String) mArraylist.get(position);


                            //opening TipsAndTricks Activity
                            openTipsAndTrickActivty("aptitude", topic, 0);

                            try {
                                Answers.getInstance().logCustom(new CustomEvent("Tips List open").putCustomAttribute("topic", topic));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });

                    topicListview.post(new Runnable() {
                        public void run() {
                            topicListview.setAdapter(adapter);
                        }
                    });


                }

            }

            @Override
            public void onTopicUpload(boolean isSuccessful) {

            }
        });


    }

    private void openTipsAndTrickActivty(String mainTopicName, String subtopicName, int subjectCode) {

        Intent intent = new Intent(StudyDataActivity.this, TipsAndTricksActivity.class);
        intent.putExtra("MainTopicName", mainTopicName);
        intent.putExtra("SubTopicName", subtopicName);
        intent.putExtra("SubjectCode", subjectCode);
        startActivity(intent);

    }


}
