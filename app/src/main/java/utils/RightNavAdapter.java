package utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.aptitude.quiz.craftystudio.aptitudequiz.R;

/**
 * Created by Aisha on 2/27/2018.
 */

public class RightNavAdapter extends ArrayAdapter<String> {


    ArrayList<Object> mQuestionNameList;
    Context mContext;

    int mResourceID;

    ClickListener clickListener;


    public RightNavAdapter(Context context, int resource, ArrayList<Object> topicList) {
        super(context, resource);
        this.mQuestionNameList = topicList;
        this.mContext = context;
        this.mResourceID = resource;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_right_side_nav, null, false);
        }
        //getting the view
        //  View view = layoutInflater.inflate(mResourceID, null, false);


        TextView questionNameTextview = (TextView) convertView.findViewById(R.id.custom_right_nav_question_name);
        TextView questionNoTextview = (TextView) convertView.findViewById(R.id.custom_right_nav_question_no);
        TextView questionTimerTextview = (TextView) convertView.findViewById(R.id.custom_right_nav_question_timer);

        questionNameTextview.setText((String) mQuestionNameList.get(position));
        questionNoTextview.setText("Question " + (position + 1));

        questionNameTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemCLickListener(view, position);
                //  Toast.makeText(mContext, "Item clicked " + mTopicList.get(position), Toast.LENGTH_SHORT).show();
            }
        });


        // Toast.makeText(mContext, "Topic set", Toast.LENGTH_SHORT).show();
        return convertView;
    }


    @Override
    public int getCount() {
        return mQuestionNameList.size();

    }

    public void setOnItemCLickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
