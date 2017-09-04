package com.appsforprogress.android.careerpath;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by ORamirez on 6/14/2017.
 */

public class QuestionListActivity extends SingleFragmentActivity
{

    private static final String EXTRA_USER_ID = "com.appsforprogress.android.careerpath.user_id";

    public static Intent getQuestionListIntent(Context packageContext, UUID userId)
    {
        Intent qlIntent = new Intent(packageContext, QuestionListActivity.class);
        qlIntent.putExtra(EXTRA_USER_ID, userId);
        return qlIntent;
    }

    @Override
    protected Fragment createFragment()
    {
        UUID userId = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        return QuestionListFragment.newInstance(userId);
        // return new QuestionListFragment();
    }
}
