package com.appsforprogress.android.careerpath.database;

/**
 * Created by ORamirez on 6/3/2017.
 */

public class CategoryDBSchema
{
    public static final class CategoryTable
    {
        // Table Name
        public static final String NAME = "categories";

        public static final class Cols
        {
            // Table Col Names:
            public static final String UUID = "uuid";
            public static final String CATEGORY = "text";
        }
    }
}
