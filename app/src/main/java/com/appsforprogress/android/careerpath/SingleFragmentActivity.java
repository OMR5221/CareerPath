package com.appsforprogress.android.careerpath;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by ORamirez on 6/14/2017.
 */

// Abstract Activity that can hold any fragment instance:
public abstract class SingleFragmentActivity extends FragmentActivity
{
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        // Get the Fragment Manager:
        FragmentManager fm = getSupportFragmentManager();

        // Give a fragment to Fragment Manager:
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null)
        {
            fragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
