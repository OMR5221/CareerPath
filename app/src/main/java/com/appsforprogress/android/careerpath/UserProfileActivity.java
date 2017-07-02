package com.appsforprogress.android.careerpath;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class UserProfileActivity extends SingleFragmentActivity
{
    private static final String EXTRA_USER_ID = "com.appsforprogress.android.mycareerpath.user_id";
    private static final String EXTRA_FIRST_NAME = "com.appsforprogress.android.mycareerpath.first_name";
    private static final String EXTRA_LAST_NAME = "com.appsforprogress.android.mycareerpath.last_name";
    private static final String EXTRA_IMAGE_LINK = "com.appsforprogress.android.mycareerpath.profile_image";

    public static Intent launchProfile(Context packageContext) //, UUID userId)
    {
        Intent intent = new Intent(packageContext, UserProfileActivity.class);
        //intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }


    public static Intent launchFBProfile(Context packageContext, String firstName, String lastName, String profileImg)
    {
        Intent userProfile = new Intent(packageContext, UserProfileActivity.class);

        userProfile.putExtra(EXTRA_FIRST_NAME, firstName);
        userProfile.putExtra(EXTRA_LAST_NAME, lastName);
        userProfile.putExtra(EXTRA_IMAGE_LINK, profileImg);

        return userProfile;
    }

    @Override
    protected Fragment createFragment()
    {
        // return new UserProfileFragment();

        // UUID userId = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);

        return UserProfileFragment.newInstance(); //userId);
    }
}
