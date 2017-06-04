package com.appsforprogress.android.careerpath.database;

/**
 * Created by ORamirez on 6/3/2017.
 */

public class QuestionDBSchema
{
    public static final class QuestionTable
    {
        // Table Name
        public static final String NAME = "questions";

        public static final class Cols
        {
            // Table Col Names:
            public static final String UUID = "uuid";
            public static final String TEXT = "text";
            public static final String ANSWERED = "answered";
            public static final String SCORE = "score";
        }
    }
}
