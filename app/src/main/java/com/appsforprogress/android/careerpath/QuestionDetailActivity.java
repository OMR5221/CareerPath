package com.appsforprogress.android.careerpath;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.UUID;


public class QuestionDetailActivity extends SingleFragmentActivity
{

    private static final String EXTRA_QUESTION_ID = "com.appsforprogress.android.careerpath.question_id";

    // Allows us to define the Intent sent to the DetailQuestionActivity:
    // Specifies the Question Id to be display in Detail View
    public static Intent detailQuestionIntent(Context packageContext, UUID questionId)
    {
        Intent dqIntent = new Intent(packageContext, QuestionDetailActivity.class);
        dqIntent.putExtra(EXTRA_QUESTION_ID, questionId);
        return dqIntent;
    }

    @Override
    // Creates fragment when List item is selected:
    protected Fragment createFragment()
    {
        UUID questionId = (UUID) getIntent().getSerializableExtra(EXTRA_QUESTION_ID);

        return QuestionDetailFragment.newInstance(questionId);
    }
}
