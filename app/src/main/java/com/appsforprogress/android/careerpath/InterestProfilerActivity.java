package com.appsforprogress.android.careerpath;

import android.support.v4.app.Fragment;

/**
 * Created by ORamirez on 6/18/2017.
 */

public class InterestProfilerActivity  extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new InterestProfilerFragment();
    }
}
