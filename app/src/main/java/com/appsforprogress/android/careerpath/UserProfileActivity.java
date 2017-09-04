package com.appsforprogress.android.careerpath;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.login.LoginResult;

public class UserProfileActivity extends SingleFragmentActivity
{
    public static final String EXTRA_USER_ID = "com.appsforprogress.android.mycareerpath.user_id";
    public static final String EXTRA_FIRST_NAME = "com.appsforprogress.android.mycareerpath.first_name";
    public static final String EXTRA_LAST_NAME = "com.appsforprogress.android.mycareerpath.last_name";
    public static final String EXTRA_IMAGE_LINK = "com.appsforprogress.android.mycareerpath.profile_image";
    public static final String EXTRA_LOGIN_RESULT = "com.appsforprogress.android.mycareerpath.login_result";
    public static final String EXTRA_USER_PROFILE = "com.appsforprogress.android.mycareerpath.user_profile";


    public static Intent upIntent(Context packageContext, String userProfile)
    {
        Intent userProfileIntent = new Intent(packageContext, UserProfileActivity.class);
        userProfileIntent.putExtra(EXTRA_USER_PROFILE, userProfile);
        /*
        userProfile.putExtra(EXTRA_FIRST_NAME, firstName);
        userProfile.putExtra(EXTRA_LAST_NAME, lastName);
        userProfile.putExtra(EXTRA_IMAGE_LINK, profileImg);
        */

        return userProfileIntent;
    }

    @Override
    protected Fragment createFragment()
    {
        // return new UserProfileFragment();

        // UUID userId = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);

        return new UserProfileFragment();
    }
}
