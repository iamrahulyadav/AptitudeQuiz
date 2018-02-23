package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aisha on 1/18/2018.
 */

public class DataBaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "questionBookmark";

    // Contacts table name
    private static final String TABLE_BOOKMARK = "bookmark";

    // Contacts Table Columns names
    private static final String KEY_NAME = "question_name";
    private static final String KEY_OPTION_A = "option_a";
    private static final String KEY_OPTION_B = "option_b";
    private static final String KEY_OPTION_C = "option_c";
    private static final String KEY_OPTION_D = "option_d";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_TOPIC_NAME = "topic_name";
    private static final String KEY_PREVIOUS_YEAR = "previous_year";
    private static final String KEY_EXPLAINATION = "explaination";
    private static final String KEY_QUESTION_UID = "question_uid";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_BOOKMARK_TABLE = "CREATE TABLE " + TABLE_BOOKMARK + "("
                + KEY_QUESTION_UID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_OPTION_A + " TEXT,"
                + KEY_OPTION_B + " TEXT,"
                + KEY_OPTION_C + " TEXT,"
                + KEY_OPTION_D + " TEXT,"
                + KEY_CORRECT_ANSWER + " TEXT,"
                + KEY_TOPIC_NAME + " TEXT,"
                + KEY_PREVIOUS_YEAR + " TEXT,"
                + KEY_EXPLAINATION + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);

        // Create tables again
        onCreate(sqLiteDatabase);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new bookmark
    public void addBookMark(Questions questions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_UID, questions.getQuestionUID()); // Question UID
        values.put(KEY_NAME, questions.getQuestionName()); // question Name
        values.put(KEY_OPTION_A, questions.getOptionA()); // option A
        values.put(KEY_OPTION_B, questions.getOptionB()); // option B
        values.put(KEY_OPTION_C, questions.getOptionC()); // option C
        values.put(KEY_OPTION_D, questions.getOptionD()); // option D
        values.put(KEY_CORRECT_ANSWER, questions.getCorrectAnswer()); // COrrect ANswer
        values.put(KEY_TOPIC_NAME, questions.getQuestionTopicName()); // Topic NAme
        values.put(KEY_PREVIOUS_YEAR, questions.getPreviousYearsName()); // Previous Year
        values.put(KEY_EXPLAINATION, questions.getQuestionExplaination()); // Question Explianation

        // Inserting Row
        db.insert(TABLE_BOOKMARK, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Bookmarks
    public ArrayList<Questions> getAllBookmarkedQuestion() {
        ArrayList<Questions> bookmarkQuestionList = new ArrayList<Questions>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Questions questions = new Questions();
                questions.setQuestionUID(cursor.getString(0));
                questions.setQuestionName(cursor.getString(1));
                questions.setOptionA(cursor.getString(2));
                questions.setOptionB(cursor.getString(3));
                questions.setOptionC(cursor.getString(4));
                questions.setOptionD(cursor.getString(5));
                questions.setCorrectAnswer(cursor.getString(6));
                questions.setQuestionTopicName(cursor.getString(7));
                questions.setPreviousYearsName(cursor.getString(8));
                questions.setQuestionExplaination(cursor.getString(9));

                // Adding bookmark_question to list
                bookmarkQuestionList.add(questions);

            } while (cursor.moveToNext());
        }

        // return bookmark_question list
        return bookmarkQuestionList;
    }


    // Deleting single contact
    public void deleteBookmarkQuestion(Questions questions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARK, KEY_QUESTION_UID + " = ?",
                new String[]{questions.getQuestionUID()});
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOKMARK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
