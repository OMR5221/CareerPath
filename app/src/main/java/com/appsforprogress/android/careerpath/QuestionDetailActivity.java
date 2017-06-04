package com.appsforprogress.android.careerpath;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class QuestionDetailActivity extends FragmentActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // Get the Fragment Manager:
        FragmentManager fm = getSupportFragmentManager();

        // Give a fragment to Fragment Manager:
        Fragment fragment = fm.findFragmentById(R.id.quiz_detail_container);

        if (fragment == null)
        {
            fragment = new QuestionDetailFragment();
            fm.beginTransaction()
                    .add(R.id.question_detail_container, fragment)
                    .commit();
        }
    }
}
