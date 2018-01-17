package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import utils.FireBaseHandler;

public class TipsAndTricksActivity extends AppCompatActivity {

    String mTopicName;
    WebView webView;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_and_tricks);
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

        mTopicName = getIntent().getStringExtra("TopicName");

        toolbar.setTitle(mTopicName);
        setSupportActionBar(toolbar);

        showDialog();

        //download data from database

        if (mTopicName.equalsIgnoreCase("H.C.F and L.C.M"))
            mTopicName = "HCF and LCM";

        downloadTips(mTopicName);


    }

    private void downloadTips(String mTopicName) {
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadTipsAndTricks(mTopicName, new FireBaseHandler.OnTipsAndTrickslistener() {
            @Override
            public void onTipsDownLoad(String tips, boolean isSuccessful) {
                if (isSuccessful) {
                    if (tips != null) {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", tips, "text/html", "UTF-8", "");

                    } else {
                        webView = (WebView) findViewById(R.id.displayText_TipsAndTricks_Webview);
                        webView.loadDataWithBaseURL("", "Data Cannot be loaded", "text/html", "UTF-8", "");

                    }
                }
                hideDialog();
            }
        });

    }

    void showDialog() {
        progressDialog = new ProgressDialog(TipsAndTricksActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    void hideDialog() {
        progressDialog.hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
