package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;


import java.util.ArrayList;

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
    TextView optionD, questionExplaination;

    CardView optionACardView;
    CardView optionBCardView;
    CardView optionCCardView;
    CardView optionDCardView;

    CardView explainationDisplayCardview;


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
        optionA = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionA_Textview);
        optionB = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionB_Textview);
        optionC = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionC_Textview);
        optionD = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_optionD_Textview);
        TextView previousYearQuestions = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_previousYearName_Textview);
        //   TextView randomNumber = (TextView) view.findViewById(R.id.fragmentAptitudeQuiz_randomNumber_Textview);


        optionACardView = (CardView) view.findViewById(R.id.fragmentAptitudeQuiz_optionA_Cardview);
        optionBCardView = (CardView) view.findViewById(R.id.fragmentAptitudeQuiz_optionB_Cardview);
        optionCCardView = (CardView) view.findViewById(R.id.fragmentAptitudeQuiz_optionC_Cardview);
        optionDCardView = (CardView) view.findViewById(R.id.fragmentAptitudeQuiz_optionD_Cardview);


        questionName.setText("Q. " + questions.getQuestionName());
        optionA.setText(questions.getOptionA());
        optionB.setText(questions.getOptionB());
        optionC.setText(questions.getOptionC());
        optionD.setText(questions.getOptionD());
        //   randomNumber.setText(questions.getRandomNumber() + "");
        previousYearQuestions.setText(questions.getPreviousYearsName());


        optionACardView.setOnClickListener(this);
        optionBCardView.setOnClickListener(this);
        optionCCardView.setOnClickListener(this);
        optionDCardView.setOnClickListener(this);


        questionExplaination = (TextView) view.findViewById(R.id.question_explaination_textview);
        explainationDisplayCardview = (CardView) view.findViewById(R.id.question_explaination_cardview);

        getUserAnswers();

        initializeNativeAd(view);


        return view;
    }

    private void initializeNativeAd(final View view) {

        final NativeAd nativeAd = questions.getNativeAd();
        if (nativeAd != null) {

            if (nativeAd.isAdLoaded()) {

                CardView adContainer = (CardView) view.findViewById(R.id.nativeAd_view_container);
                adContainer.setVisibility(View.VISIBLE);

                adContainer.removeAllViews();

                View adView = NativeAdView.render(getContext(), nativeAd, NativeAdView.Type.HEIGHT_300);
                // Add the Native Ad View to your ad container
                adContainer.addView(adView);


            } else {

                nativeAd.setAdListener(new AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        if (view != null) {

                            try {
                                CardView adContainer = (CardView) view.findViewById(R.id.nativeAd_view_container);
                                adContainer.setVisibility(View.VISIBLE);


                                adContainer.removeAllViews();

                                View adView = NativeAdView.render(getContext(), nativeAd, NativeAdView.Type.HEIGHT_300);
                                // Add the Native Ad View to your ad container
                                adContainer.addView(adView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });

                View adContainer = view.findViewById(R.id.nativeAd_view_container);
                adContainer.setVisibility(View.GONE);

            }
        } else {
            View adContainer = view.findViewById(R.id.nativeAd_view_container);
            adContainer.setVisibility(View.GONE);

        }

    }


    private void getRightAnswer() {
        String correctANswer = questions.getCorrectAnswer().trim();

        if (questions.getOptionA().trim().equalsIgnoreCase(correctANswer)) {
            //optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            optionACardView.setBackgroundResource(R.drawable.mygreenbutton);


        } else if (questions.getOptionB().trim().equalsIgnoreCase(correctANswer)) {
            //optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            optionBCardView.setBackgroundResource(R.drawable.mygreenbutton);


        } else if (questions.getOptionC().trim().equalsIgnoreCase(correctANswer)) {
            //optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            optionCCardView.setBackgroundResource(R.drawable.mygreenbutton);


        } else if (questions.getOptionD().trim().equalsIgnoreCase(correctANswer)) {
            //optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            optionDCardView.setBackgroundResource(R.drawable.mygreenbutton);

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
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        progressDialog.cancel();
    }

    @Override
    public void onClick(View view) {

        //normal topic questions and normal test series
        if (!RandomTestActivity.isRandomTestQuestions) {


            switch (view.getId()) {

                case R.id.fragmentAptitudeQuiz_optionA_Cardview:
                    questions.setUserAnswer(questions.getOptionA());
                    if (questions.getOptionA().equalsIgnoreCase(questions.getCorrectAnswer())) {
                        Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
                        //optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionACardView.setBackgroundResource(R.drawable.mygreenbutton);


                    } else {
                        // Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        // optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionACardView.setBackgroundResource(R.drawable.myredbutton);

                        getRightAnswer();
                    }
                    break;

                case R.id.fragmentAptitudeQuiz_optionB_Cardview:
                    questions.setUserAnswer(questions.getOptionB());

                    if (questions.getOptionB().equalsIgnoreCase(questions.getCorrectAnswer())) {
                        Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
                        // optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionBCardView.setBackgroundResource(R.drawable.mygreenbutton);

                    } else {
                        // Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        // optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionBCardView.setBackgroundResource(R.drawable.myredbutton);
                        getRightAnswer();
                    }
                    break;
                case R.id.fragmentAptitudeQuiz_optionC_Cardview:
                    questions.setUserAnswer(questions.getOptionC());

                    if (questions.getOptionC().equalsIgnoreCase(questions.getCorrectAnswer())) {
                        Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
                        // optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionCCardView.setBackgroundResource(R.drawable.mygreenbutton);

                    } else {
                        //  Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        // optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionCCardView.setBackgroundResource(R.drawable.myredbutton);

                        getRightAnswer();
                    }
                    break;
                case R.id.fragmentAptitudeQuiz_optionD_Cardview:
                    questions.setUserAnswer(questions.getOptionD());

                    if (questions.getOptionD().equalsIgnoreCase(questions.getCorrectAnswer())) {
                        Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
                        //optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionDCardView.setBackgroundResource(R.drawable.mygreenbutton);

                    } else {
                        // Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        // optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionDCardView.setBackgroundResource(R.drawable.myredbutton);

                        getRightAnswer();
                    }
                    break;
                case R.id.fragmentAptitudeQuiz_topicName_Textview:
                    //open TOPIC ACTIVITY
                    Intent intent = new Intent(mainActivity, TopicActivity.class);
                    startActivity(intent);
                    break;
            }

            //setExplaination
            explainationDisplayCardview.setVisibility(View.VISIBLE);
            questionExplaination.setText(questions.getQuestionExplaination());


        } else {


            optionDCardView.setCardBackgroundColor(Color.WHITE);
            optionACardView.setCardBackgroundColor(getResources().getColor(R.color.colorWhiteBg));
            optionBCardView.setCardBackgroundColor(getResources().getColor(R.color.colorWhiteBg));
            optionCCardView.setCardBackgroundColor(getResources().getColor(R.color.colorWhiteBg));
            optionDCardView.setCardBackgroundColor(getResources().getColor(R.color.colorWhiteBg));

            // optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteBg));
            //  optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteBg));
            //  optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteBg));
            //  optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteBg));

            CardView testCardview = (CardView) view;
            //linearLayout.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
            testCardview.setBackgroundResource(R.drawable.mybutton);

            switch (view.getId()) {

                case R.id.fragmentAptitudeQuiz_optionA_Cardview:
                    questions.setUserAnswer(questions.getOptionA());
                    break;
                case R.id.fragmentAptitudeQuiz_optionB_Cardview:
                    questions.setUserAnswer(questions.getOptionB());
                    break;
                case R.id.fragmentAptitudeQuiz_optionC_Cardview:
                    questions.setUserAnswer(questions.getOptionC());
                    break;
                case R.id.fragmentAptitudeQuiz_optionD_Cardview:
                    questions.setUserAnswer(questions.getOptionD());
                    break;


            }
        }

        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentId("question answer"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUserAnswers() {

        if (!RandomTestActivity.isRandomTestQuestions) {


            if (questions.getUserAnswer() != null) {


                //setExplaination
                explainationDisplayCardview.setVisibility(View.VISIBLE);
                questionExplaination.setText(questions.getQuestionExplaination());


                if (questions.getUserAnswer().equalsIgnoreCase(questions.getCorrectAnswer())) {
                    if (questions.getOptionA().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionACardView.setBackgroundResource(R.drawable.mygreenbutton);


                    } else if (questions.getOptionB().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionBCardView.setBackgroundResource(R.drawable.mygreenbutton);


                    } else if (questions.getOptionC().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        // optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionCCardView.setBackgroundResource(R.drawable.mygreenbutton);

                    } else if (questions.getOptionD().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        optionDCardView.setBackgroundResource(R.drawable.mygreenbutton);

                    }

                } else {
                    if (questions.getOptionA().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionACardView.setBackgroundResource(R.drawable.myredbutton);


                    } else if (questions.getOptionB().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionBCardView.setBackgroundResource(R.drawable.myredbutton);


                    } else if (questions.getOptionC().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        // optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionCCardView.setBackgroundResource(R.drawable.myredbutton);

                    } else if (questions.getOptionD().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                        //optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        optionDCardView.setBackgroundResource(R.drawable.myredbutton);

                    }


                }

            }
        } else {
            if (questions.getUserAnswer() != null) {

                if (questions.getOptionA().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                    //optionACardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                    optionACardView.setBackgroundResource(R.drawable.mybutton);


                } else if (questions.getOptionB().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                    //optionBCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                    optionBCardView.setBackgroundResource(R.drawable.mybutton);



                } else if (questions.getOptionC().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                    //optionCCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                    optionCCardView.setBackgroundResource(R.drawable.mybutton);

                 } else if (questions.getOptionD().trim().equalsIgnoreCase(questions.getUserAnswer().trim())) {
                    //optionDCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                    optionDCardView.setBackgroundResource(R.drawable.mybutton);


                }

            }
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
