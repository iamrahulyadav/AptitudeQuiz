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

public class MenuPracticeFragment extends Fragment {

    CardView aptitudeCardview, verbalCardview, logicalCardview, computerCardview;

    int study = 0;
    int subject = 0;

    //subject code 0= aptitude, 1= verbal, 2 = logical, 3 = computer
    //study=0; during practice


    public MenuPracticeFragment() {
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

        View view = inflater.inflate(R.layout.fragment_practice_menu, container, false);

        aptitudeCardview = (CardView) view.findViewById(R.id.fragment_practice_menu_aptitude_cardview);
        verbalCardview = (CardView) view.findViewById(R.id.fragment_practice_menu_verbal_cardview);
        logicalCardview = (CardView) view.findViewById(R.id.fragment_practice_menu_logical_cardview);
        computerCardview = (CardView) view.findViewById(R.id.fragment_practice_menu_computer_cardview);

        aptitudeCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(0, 0);
            }
        });
        logicalCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(0, 2);
            }
        });
        verbalCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(0, 1);
            }
        });
        computerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTopicActivity(0, 3);
            }
        });

        return view;
    }

    private void callTopicActivity(int study, int subject) {

        Intent intent = new Intent(getContext(), TopicActivity.class);
        intent.putExtra("study", study);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }
}
