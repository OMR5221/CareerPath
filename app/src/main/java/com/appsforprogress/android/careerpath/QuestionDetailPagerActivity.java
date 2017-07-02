package com.appsforprogress.android.careerpath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

/**
 * Created by ORamirez on 6/28/2017.
 */

public class QuestionDetailPagerActivity extends FragmentActivity
{
    private ViewPager mViewPager;
    private List<Question> mQuestions;


    private static final String EXTRA_QUESTION_ID = "com.appsforprogress.android.careerpath.question_id";

    // Allows us to define the Intent sent to the DetailQuestionActivity:
    // Specifies the Question Id to be display in Detail View
    public static Intent detailQuestionIntent(Context packageContext, UUID questionId)
    {
        Intent dqIntent = new Intent(packageContext, QuestionDetailPagerActivity.class);
        dqIntent.putExtra(EXTRA_QUESTION_ID, questionId);
        return dqIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail_pager);

        UUID questionId = (UUID) getIntent().getSerializableExtra(EXTRA_QUESTION_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_question_detail_viewPager);

        mQuestions = Quiz.get(this).getQuestions();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                Question question = mQuestions.get(position);
                // Return Fragment for detail Question View:
                return QuestionDetailFragment.newInstance(question.getId());
            }

            @Override
            public int getCount() {
                return mQuestions.size();
            }
        });


        for (int i = 0; i < mQuestions.size(); i++)
        {
            if (mQuestions.get(i).getId().equals(questionId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
