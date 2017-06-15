package com.appsforprogress.android.careerpath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appsforprogress.android.careerpath.database.CareerPathBaseHelper;
import com.appsforprogress.android.careerpath.database.QuestionCursorWrapper;
import com.appsforprogress.android.careerpath.database.QuestionDBSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.appsforprogress.android.careerpath.database.QuestionDBSchema.*;
import static java.lang.Boolean.FALSE;

/**
 * Created by ORamirez on 5/30/2017.
 */

public class Quiz
{
    private static Quiz sQuiz;
    private Context mContext;
    private SQLiteDatabase mQuestionsDB;
    private List<Question> mQuestions;

    private Quiz(Context context)
    {
        mContext = context.getApplicationContext();
        // Use the SqLiteDBHelper to generate a DB
        mQuestionsDB = new CareerPathBaseHelper(mContext).getWritableDatabase();
        // Generate empty List of Questions to fill with data from DB:
        mQuestions = new ArrayList<>();

        for (int i = 0; i < 12; i++)
        {
            Question question = new Question();

            if (i == 0)
            {
                question.setText("Build kitchen cabinets");
                question.setCategory("REALISTIC");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 1)
            {
                question.setText("Guard money in an armored car");
                question.setCategory("REALISTIC");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 2)
            {
                question.setText("Study space travel");
                question.setCategory("INVESTIGATIVE");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 3)
            {
                question.setText("Map the bottom of the ocean");
                question.setCategory("INVESTIGATIVE");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 4)
            {
                question.setText("Conduct a symphony orchestra");
                question.setCategory("ARTISTIC");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 5)
            {
                question.setText("Write stories or articles for a magazine");
                question.setCategory("ARTISTIC");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 6)
            {
                question.setText("Teach an exercise routine");
                question.setCategory("SOCIAL");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 7)
            {
                question.setText("Perform nursing duties in a hospital");
                question.setCategory("SOCIAL");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 8)
            {
                question.setText("Buy and sell stocks and bonds");
                question.setCategory("ENTERPRISING");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 9)
            {
                question.setText("Manage a retail store");
                question.setCategory("ENTERPRISING");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 10)
            {
                question.setText("Develop computer software");
                question.setCategory("CONVENTIONAL");
                question.setAnswered(FALSE);
                question.setScore(0);
            }
            else if (i == 11)
            {
                question.setText("Proofread records or forms");
                question.setCategory("CONVENTIONAL");
                question.setAnswered(FALSE);
                question.setScore(0);
            }

            mQuestions.add(question);
        }


    }

    // Return existing singleton of Questions or create a new instance
    // Need to create a DB instead:
    public static Quiz get(Context context)
    {
        if (sQuiz == null)
        {
            sQuiz = new Quiz(context);
        }

        return sQuiz;
    }

    // Create ContentValues to write and update DB:
    private static ContentValues getContentValues(Question question)
    {
        // Content Values Hash to get Question frontend details back to DB:
        ContentValues cvals = new ContentValues();
        cvals.put(QuestionTable.Cols.UUID, question.getId().toString());
        cvals.put(QuestionTable.Cols.TEXT, question.getText());
        cvals.put(QuestionTable.Cols.ANSWERED, question.getAnswered() ? 1:0);
        cvals.put(QuestionTable.Cols.SCORE, question.getScore());
        cvals.put(QuestionTable.Cols.CATEGORY, question.getCategory());

        return cvals;
    }

    // Add user created Question to DB:
    public void addQuestion(Question q)
    {
        ContentValues cVals = getContentValues(q);
        mQuestionsDB.insert(QuestionTable.NAME, null, cVals);
    }

    public void updateQuestion(Question q)
    {
        String uuidString = q.getId().toString();

        ContentValues cVals = getContentValues(q);

        // Specify the row to update with the Content Values Hash:
        mQuestionsDB.update(QuestionTable.NAME, cVals,
                            QuestionTable.Cols.UUID + " = ?",
                            new String[] {uuidString});
    }

    private QuestionCursorWrapper queryQuestions(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mQuestionsDB.query(
                QuestionTable.NAME,
                null, // Select all Columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new QuestionCursorWrapper(cursor);
    }

    // Use QueryWrapper to get raw data and generate Question Objects:
    public List<Question> getQuestions()
    {
        List<Question> questions = new ArrayList<>();

        // Get all Question records from DB:
        QuestionCursorWrapper cursor = queryQuestions(null, null);

        try {
            //  Get first data record
            cursor.moveToFirst();

            // Iterate over remaining records:
            while (!cursor.isAfterLast())
            {
                questions.add(cursor.getQuestion());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return questions;
    }

    public Question getQuestion(UUID id)
    {
        QuestionCursorWrapper cursor = queryQuestions(
                QuestionTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0)
            {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getQuestion();

        } finally {
            cursor.close();
        }
    }


}
