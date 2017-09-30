package app.aptitude.quiz.craftystudio.aptitudequiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import utils.Questions;

/**
 * Created by Aisha on 9/30/2017.
 */

public class AptitudeFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    ProgressDialog progressDialog;
    static MainActivity mainActivity;

    Questions questions;

    TextView optionA;
    TextView optionB;
    TextView optionC;
    TextView optionD;

    public static AptitudeFragment newInstance(Questions questions, MainActivity context) {
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


        questionName.setText("Q. " + questions.getQuestionName());
        questionExplaination.setText(questions.getQuestionExplaination());
        optionA.setText(questions.getOptionA() + "");
        optionB.setText(questions.getOptionB() + "");
        optionC.setText(questions.getOptionC() + "");
        optionD.setText(questions.getOptionD() + "");
        randomNumber.setText(questions.getRandomNumber() + "");
        questionTopicName.setText(questions.getQuestionTopicName());
        previousYearQuestions.setText(questions.getPreviousYearsName());

        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);


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

        TextView textview = (TextView) view;
        if (textview.getText().toString().equalsIgnoreCase(questions.getCorrectAnswer())) {
            Toast.makeText(mainActivity, "Right Answer", Toast.LENGTH_SHORT).show();
            textview.setTextColor(Color.GREEN);

        } else {
            Toast.makeText(mainActivity, "Wrong Answer", Toast.LENGTH_SHORT).show();
            textview.setTextColor(Color.RED);
            getRightAnswer();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
