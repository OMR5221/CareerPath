package com.appsforprogress.android.careerpath;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.appsforprogress.android.careerpath.database.CareerPathBaseHelper;
import com.appsforprogress.android.careerpath.database.QuestionDBSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.appsforprogress.android.careerpath.database.QuestionDBSchema.*;

/**
 * Created by ORamirez on 5/30/2017.
 */

public class Quiz
{
    private static Quiz sQuiz;
    private Context mContext;
    private SQLiteDatabase mQuestionsDB;

    private Quiz(Context context)
    {
        mContext = context.getApplicationContext();
        // Use the SqLiteDBHelper to generate a DB
        mQuestionsDB = new CareerPathBaseHelper(mContext).getWritableDatabase();
        // Generate empty List of Questions to fill with data from DB:

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

        return cvals;
    }

    public List<Question> getQuestions()
    {
        return new ArrayList<>();
    }

    public Question getQuestion(UUID id)
    {
        return null;
    }

    // Add user created Question to DB:
    public void addQuestion(Question q)
    {
        ContentValues cvals = getContentValues(q);
        mQuestionsDB.insert(QuestionTable.NAME, null, cvals);
    }

    public void updateQuestion(Question q)
    {
        String uuidString = q.getId().toString();

        ContentValues cvals = getContentValues(q);

        // Specify the row to update with the Content Values Hash:
        mQuestionsDB.update(QuestionTable.NAME, cvals,
                            QuestionTable.Cols.UUID + " = ?",
                            new String[] {uuidString});
    }
}
