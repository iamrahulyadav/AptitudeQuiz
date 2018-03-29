package app.aptitude.quiz.craftystudio.aptitudequiz;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aisha on 3/22/2018.
 */

public class MenuStudyFragment extends Fragment {

    CardView aptitudeCardview, englishCardview, computerCardview;

    int study = 1;
    int subject = 0;

    //subject code 0= aptitude, 1= english, 2 = computer
    //study=1; during study


    public MenuStudyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_menu, container, false);

        aptitudeCardview = (CardView) view.findViewById(R.id.fragment_study_menu_aptitude_cardview);
        englishCardview = (CardView) view.findViewById(R.id.fragment_study_menu_english_cardview);
        computerCardview = (CardView) view.findViewById(R.id.fragment_study_menu_computer_cardview);

        aptitudeCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(1, 0);
            }
        });

        englishCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(1, 1);
            }
        });
        computerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(1, 2);
            }
        });

        return view;
    }

    private void callTopicActivity(int study, int subject) {

        Intent intent = new Intent(getContext(), StudyDataActivity.class);
        intent.putExtra("study", study);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }


}
