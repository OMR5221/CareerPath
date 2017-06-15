package com.appsforprogress.android.careerpath;

import android.support.v4.app.Fragment;

/**
 * Created by ORamirez on 6/14/2017.
 */

public class QuestionListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new QuestionListFragment();
    }
}
