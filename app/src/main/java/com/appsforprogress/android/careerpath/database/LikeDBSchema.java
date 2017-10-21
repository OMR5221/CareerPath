package com.appsforprogress.android.careerpath.database;

/**
 * Created by ORamirez on 6/3/2017.
 */

public class LikeDBSchema
{
    public static final class UserTable
    {
        // Table Name
        public static final String NAME = "user_likes";

        public static final class Cols
        {
            // Table Col Names:

            public static final String LIKEID = "id";
            public static final String NAME = "name";
            public static final String CATEGORY = "category";
            public static final String PICLINK = "pic_link";
        }
    }
}
