package com.appsforprogress.android.careerpath.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appsforprogress.android.careerpath.database.QuestionDBSchema.QuestionTable;

/**
 * Created by ORamirez on 6/3/2017.
 */

public class CareerPathBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "questionBase.db";

    public CareerPathBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Code to create the initial DB:

        // Create Question Table:
        db.execSQL( "create table " + QuestionTable.NAME +
                    "(" +
                        " _id integer primary key autoincrement, " +
                        QuestionTable.Cols.UUID + ", " +
                        QuestionTable.Cols.TEXT + ", " +
                        QuestionTable.Cols.ANSWERED + ", " +
                        QuestionTable.Cols.SCORE + ", " +
                        QuestionTable.Cols.CATEGORY
                    + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
