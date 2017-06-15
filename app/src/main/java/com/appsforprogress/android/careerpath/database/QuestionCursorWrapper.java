package com.appsforprogress.android.careerpath.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.appsforprogress.android.careerpath.Question;

import java.util.UUID;

import static com.appsforprogress.android.careerpath.database.QuestionDBSchema.*;

/**
 * Created by ORamirez on 6/11/2017.
 */

// Uses our Cursor in Quiz to pull records from DB:
public class QuestionCursorWrapper extends CursorWrapper
{
    public QuestionCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Question getQuestion()
    {
        String uuidString = getString(getColumnIndex(QuestionTable.Cols.UUID));
        String text = getString(getColumnIndex(QuestionTable.Cols.TEXT));
        int answered = getInt(getColumnIndex(QuestionTable.Cols.ANSWERED));
        int score = getInt(getColumnIndex(QuestionTable.Cols.SCORE));
        String category = getString(getColumnIndex(QuestionTable.Cols.CATEGORY));

        // Create question
        Question question = new Question(UUID.fromString(uuidString));
        question.setText(text);
        question.setAnswered(answered != 0);
        question.setScore(score);
        question.setCategory(category);

        return question;
    }

}
