package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;


import utils.Questions;

/**
 * Created by Aisha on 9/30/2017.
 */

public class AptitudeFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    ProgressDialog progressDialog;
    static Context mainActivity;

    Questions questions;

    TextView optionA;
    TextView optionB;
    TextView optionC;
    TextView optionD;


    public static AptitudeFragment newInstance(Questions questions, Context context, boolean randomTestOn) {

        if (randomTestOn) {
            RandomTestActivity.isRandomTestQuestions = true;
        }

        mainActivity = context;
        AptitudeFragment fragment = new AptitudeFragment();
        Bundle args = new Bundle();
        args.putSerializable("Question", questions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.questions = (Questions) getArguments().getSerializable("Question");

           /* Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(story.getStoryTitle())
                    .putContentId(story.getStoryID())
            );
            */

        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aptitude_quiz, container, false);
        //initializeView

        TextView questionName = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_QuestionName_Textview);
        TextView questionExplaination = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_explaination_Textview);
        optionA = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionA_Textview);
        optionB = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionB_Textview);
        optionC = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionC_Textview);
        optionD = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionD_Textview);
        TextView questionTopicName = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_topicName_Textview);
        TextView previousYearQuestions = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_previousYearName_Textview);
        TextView randomNumber = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_randomNumber_Textview);

        Button shareQuestionButton = (Button) view.findViewById(R.id.fragmentAptitudeQuiz_share_button);

        shareQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareClick();
            }
        });


        questionName.setText("Q. " + questions.getQuestionName());
        optionA.setText(questions.getOptionA() + "");
        optionB.setText(questions.getOptionB() + "");
        optionC.setText(questions.getOptionC() + "");
        optionD.setText(questions.getOptionD() + "");
        randomNumber.setText(questions.getRandomNumber() + "");
        questionTopicName.setText(questions.getQuestionTopicName());
        previousYearQuestions.setText(questions.getPreviousYearsName());

        if (RandomTestActivity.isRandomTestQuestions) {
            questionExplaination.setText("Complete the test first");
        } else {
            questionExplaination.setText(questions.getQuestionExplaination());
        }
        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);


        getUserAnswers();


        return view;
    }


    private void getRightAnswer() {
        String correctANswer = questions.getCorrectAnswer();

        if (optionA.getText().toString().equalsIgnoreCase(correctANswer)) {
            optionA.setTextColor(Color.GREEN);

        } else if (optionB.getText().toString().equalsIgnoreCase(correctANswer)) {
            optionB.setTextColor(Color.GREEN);

        } else if (optionC.getText().toString().equalsIgnoreCase(correctANswer)) {
            optionC.setTextColor(Color.GREEN);

        } else if (optionD.getText().toString().equalsIgnoreCase(correctANswer)) {
            optionD.setTextColor(Color.GREEN);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showDialog() {
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Please wait..Creating link");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void hideDialog() {
        progressDialog.cancel();
    }

    @Override
    public void onClick(View view) {

        //normal topic questions and normal test series
        if (!RandomTestActivity.isRandomTestQuestions) {

            TextView textview = (TextView) view;
            if (textview.getText().toString().equalsIgnoreCase(questions.getCorrectAnswer())) {
                Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
                textview.setTextColor(Color.GREEN);

            } else {
                Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
                textview.setTextColor(Color.RED);
                getRightAnswer();
            }

        } else {
            TextView textview = (TextView) view;
            textview.setTextColor(Color.MAGENTA);
            questions.setUserAnswer(textview.getText().toString());
        }

    }

    private void getUserAnswers() {

        if (questions.getUserAnswer() != null) {


            if (questions.getUserAnswer().equalsIgnoreCase(questions.getCorrectAnswer())) {
                if (optionA.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionA.setTextColor(Color.GREEN);

                } else if (optionB.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionB.setTextColor(Color.GREEN);


                } else if (optionC.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionC.setTextColor(Color.GREEN);

                } else if (optionD.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionD.setTextColor(Color.GREEN);

                }

            } else {
                if (optionA.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionA.setTextColor(Color.RED);

                } else if (optionB.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionB.setTextColor(Color.RED);


                } else if (optionC.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionC.setTextColor(Color.RED);

                } else if (optionD.getText().toString().equalsIgnoreCase(questions.getUserAnswer())) {
                    optionD.setTextColor(Color.RED);

                }
            }
        }

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
