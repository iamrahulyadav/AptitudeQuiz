package utils;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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


    static ArrayList<ValueEventListener> valueEventListenerArrayList = new ArrayList<>();
    static ArrayList<Query> databaseReferenceArrayList = new ArrayList<>();


    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public FireBaseHandler() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

    public static void removeListener() {
        for (int i = 0; i < valueEventListenerArrayList.size(); i++) {
            databaseReferenceArrayList.get(i).removeEventListener(valueEventListenerArrayList.get(i));

        }
        databaseReferenceArrayList.clear();
        valueEventListenerArrayList.clear();
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


    public void uploadTestName(final String test, final OnTestSerieslistener onTestSerieslistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("TestSeries/");


        DatabaseReference mDatabaseRef1 = mFirebaseDatabase.getReference().child("TestSeries/" + mDatabaseRef.push().getKey());


        mDatabaseRef1.setValue(test).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onTestSerieslistener.onTestDownLoad(test, true);
                onTestSerieslistener.onTestUpload(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failed to Upload Story", e.getMessage());

                onTestSerieslistener.onTestUpload(false);
                onTestSerieslistener.onTestDownLoad(null, false);
            }
        });


    }

    public void downloadTestList(int limit, final OnTestSerieslistener onTestSerieslistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("TestSeries/");

        Query myref2 = mDatabaseRef.orderByKey().limitToLast(limit);

        databaseReferenceArrayList.add(myref2);

        ValueEventListener valueEventListener = myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> testArrayList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String test = snapshot.getValue(String.class);
                    if (test != null) {
                        testArrayList.add(test);

                    }
                }

                Collections.reverse(testArrayList);

                onTestSerieslistener.onTestListDownLoad(testArrayList, true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onTestSerieslistener.onTestListDownLoad(null, false);

            }
        });

        valueEventListenerArrayList.add(valueEventListener);


    }


    public void uploadTopicName(final String topic, final OnTopiclistener onTopiclistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Topic/");


        DatabaseReference mDatabaseRef1 = mFirebaseDatabase.getReference().child("Topic/" + mDatabaseRef.push().getKey());

        //DatabaseReference mDatabaseRef1 = mFirebaseDatabase.getReference().child("Topic/");


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

    public void downloadTopicList(int limit, final OnTopiclistener onTopiclistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Topic/");

        Query myref2 = mDatabaseRef.orderByKey().limitToLast(limit);

        databaseReferenceArrayList.add(myref2);

        ValueEventListener valueEventListener = myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> topicArrayList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String topic = snapshot.getValue(String.class);
                    if (topic != null) {
                        topicArrayList.add(topic);

                    }
                }

                Collections.reverse(topicArrayList);

                onTopiclistener.onTopicListDownLoad(topicArrayList, true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onTopiclistener.onTopicListDownLoad(null, false);

            }
        });

        valueEventListenerArrayList.add(valueEventListener);


    }

    public void downloadQuestion(String questionUID, final OnQuestionlistener onQuestionlistener) {


        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Questions/" + questionUID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Questions question = dataSnapshot.getValue(Questions.class);

                if (question != null) {
                    question.setQuestionUID(dataSnapshot.getKey());
                }
                onQuestionlistener.onQuestionDownLoad(question, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onQuestionlistener.onQuestionDownLoad(null, false);
            }
        });


    }

    public void downloadTipsAndTricks(String topicName, final OnTipsAndTrickslistener onTipsAndTrickslistener) {


        DatabaseReference myRef = mFirebaseDatabase.getReference().child("TipsAndTricks/" + topicName);

        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String tipsData = dataSnapshot.getValue(String.class);


                onTipsAndTrickslistener.onTipsDownLoad(tipsData, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onTipsAndTrickslistener.onTipsDownLoad(null, false);
            }
        });

        valueEventListenerArrayList.add(valueEventListener);


    }

    //random no generate
    final int min = 1;
    final int max = 100;
    Random random = new Random();
    final int r = random.nextInt((max - min) + 1) + min;

    public void downloadQuestionList(int limit, final OnQuestionlistener onQuestionlistener) {


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

    public void downloadMoreQuestionsList(String topicName, int limit, String UID, final OnQuestionlistener onQuestionlistener) {

        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        Query myref2 = mDatabaseRef.orderByChild("questionTopicName").endAt(topicName, UID).limitToLast(limit);

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

                questionsArrayList.remove(0);
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
        databaseReferenceArrayList.add(myref2);


        ValueEventListener valueEventListener = myref2.addValueEventListener(new ValueEventListener() {
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

        valueEventListenerArrayList.add(valueEventListener);


/*

        myref2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "onChildAdded: "+dataSnapshot.toString());
                ArrayList<Questions> questionsArrayList = new ArrayList<Questions>();

                Questions questions = dataSnapshot.getValue(Questions.class);
                questionsArrayList.add(questions);
                onQuestionlistener.onQuestionListDownLoad(questionsArrayList, true);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "onChildAdded: "+dataSnapshot.toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", "onChildAdded: "+dataSnapshot.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "onChildAdded: "+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE", "onChildAdded: "+databaseError.toString());
            }
        });

*/

    }

    public void downloadQuestionList(int limit, String testName, final OnQuestionlistener onQuestionlistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("Questions/");

        Query myref2 = mDatabaseRef.orderByChild("questionTestName").equalTo(testName).limitToLast(limit);

        databaseReferenceArrayList.add(myref2);


        ValueEventListener valueEventListener = myref2.addValueEventListener(new ValueEventListener() {
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

        valueEventListenerArrayList.add(valueEventListener);


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

    public interface OnTipsAndTrickslistener {


        public void onTipsDownLoad(String tips, boolean isSuccessful);


    }

    public interface OnTestSerieslistener {


        public void onTestDownLoad(String test, boolean isSuccessful);

        public void onTestListDownLoad(ArrayList<String> testList, boolean isSuccessful);


        public void onTestUpload(boolean isSuccessful);
    }

}
