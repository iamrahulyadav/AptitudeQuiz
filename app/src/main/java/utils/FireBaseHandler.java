package utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Aisha on 9/30/2017.
 */

public class FireBaseHandler {

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;


    public FireBaseHandler() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }


    public void uploadQuestion(final Questions questions, final OnQuestionlistener onQuestionlistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        questions.setQuestionUID(mDatabaseRef.push().getKey());

        DatabaseReference mDatabaseRef1 = mFirebaseDatabase.getReference().child("Questions/" + questions.getQuestionUID());


        mDatabaseRef1.setValue(questions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onQuestionlistener.onQuestionDownLoad(questions, true);
                onQuestionlistener.onQuestionUpload(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failed to Upload Story", e.getMessage());

                onQuestionlistener.onQuestionUpload(false);
                onQuestionlistener.onQuestionDownLoad(null, false);
            }
        });


    }

    public void uploadTopic(final String topic, final OnTopiclistener onTopiclistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Topic/");

        DatabaseReference mDatabaseRef1 = mFirebaseDatabase.getReference().child("Topic/");


        mDatabaseRef1.setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onTopiclistener.onTopicDownLoad(topic, true);
                onTopiclistener.onTopicUpload(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failed to Upload Story", e.getMessage());

                onTopiclistener.onTopicUpload(false);
                onTopiclistener.onTopicDownLoad(null, false);
            }
        });


    }

    public void downloadQuestionList(int limit, final OnQuestionlistener onQuestionlistener) {


        //random no generate
        final int min = 1;
        final int max = 100;
        Random random = new Random();
        final int r = random.nextInt((max - min) + 1) + min;


        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        Query myref2 = mDatabaseRef.orderByChild("randomNumber").startAt(r).limitToFirst(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Questions> questionsArrayList = new ArrayList<Questions>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Questions questions = snapshot.getValue(Questions.class);
                    if (questions != null) {
                        questions.setQuestionUID(snapshot.getKey());
                    }
                    questionsArrayList.add(questions);
                }

              // Collections.reverse(questionsArrayList);

                onQuestionlistener.onQuestionListDownLoad(questionsArrayList, true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onQuestionlistener.onQuestionListDownLoad(null, false);

            }
        });


    }

    public void downloadQuestionList(int limit, int lastRandomNo, final OnQuestionlistener onQuestionlistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        // Query myref2 = mDatabaseRef.orderByKey().limitToLast(limit).endAt(lastQuestionID);

        Query myref2 = mDatabaseRef.orderByChild("randomNumber").startAt(lastRandomNo).limitToFirst(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Questions> questionsArrayList = new ArrayList<Questions>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Questions questions = snapshot.getValue(Questions.class);
                    if (questions != null) {
                        questions.setQuestionUID(snapshot.getKey());
                    }
                    questionsArrayList.add(questions);
                }

                questionsArrayList.remove(questionsArrayList.size() - 1);
              //  Collections.reverse(questionsArrayList);
                onQuestionlistener.onQuestionListDownLoad(questionsArrayList, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onQuestionlistener.onQuestionListDownLoad(null, false);

            }
        });


    }


    public void downloadQuestionList(String topicName, int limit, final OnQuestionlistener onQuestionlistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        Query myref2 = mDatabaseRef.orderByChild("questionTopicName").equalTo(topicName).limitToLast(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Questions> questionsArrayList = new ArrayList<Questions>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Questions questions = snapshot.getValue(Questions.class);
                    if (questions != null) {

                        questions.setQuestionUID(snapshot.getKey());

                    }
                    questionsArrayList.add(questions);
                }

                Collections.reverse(questionsArrayList);

                onQuestionlistener.onQuestionListDownLoad(questionsArrayList, true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onQuestionlistener.onQuestionListDownLoad(null, false);

            }
        });


    }

    public interface OnQuestionlistener {


        public void onQuestionDownLoad(Questions questions, boolean isSuccessful);

        public void onQuestionListDownLoad(ArrayList<Questions> questionList, boolean isSuccessful);


        public void onQuestionUpload(boolean isSuccessful);
    }

    public interface OnTopiclistener {


        public void onTopicDownLoad(String topic, boolean isSuccessful);

        public void onTopicListDownLoad(ArrayList<String> topicList, boolean isSuccessful);


        public void onTopicUpload(boolean isSuccessful);
    }

}
