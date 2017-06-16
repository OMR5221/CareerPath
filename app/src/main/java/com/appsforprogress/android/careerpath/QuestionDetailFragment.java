package com.appsforprogress.android.careerpath;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by ORamirez on 5/29/2017.
 */

public class QuestionDetailFragment extends Fragment
{
    // Question object to display in Fragment:
    private Question mQuestion;
    private TextView mQuestionText;
    private RadioGroup mAnswerRG;

    @Override
    // Configure Fragment:
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Question ID from Intent in Activity:
        UUID questionId = (UUID) getActivity()
                .getIntent()
                .getSerializableExtra(QuestionDetailActivity.EXTRA_QUESTION_ID);

        // Use our Activity to communicate with the Quiz Object holding the Questions created from DB data
        mQuestion = Quiz.get(getActivity()).getQuestion(questionId);
    }

    @Override
    // Refreshes the Detail Fragment with the Question selected from Question Listing
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        // Configure View for Fragment:
        View v = inflater.inflate(R.layout.fragment_question_detail, container, false);

        // Setup TextView for Question text:
        mQuestionText = (TextView) v.findViewById(R.id.question_detail_text);
        // Set Question Text data
        mQuestionText.setText(mQuestion.getText());

        // Get Radio Group for possible answers
        mAnswerRG = (RadioGroup) v.findViewById(R.id.question_answer_group);

        // Listen for Radio Button selection:
        mAnswerRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.strongly_agree_button:
                        mQuestion.setScore(5);
                    case R.id.agree_button:
                        mQuestion.setScore(4);
                    case R.id.neutral_button:
                        mQuestion.setScore(3);
                    case R.id.disagree_button:
                        mQuestion.setScore(2);
                    case R.id.strongly_disagree_button:
                        mQuestion.setScore(1);
                }
            }
        });

        return v;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Quiz.get(getActivity()).updateQuestion(mQuestion);
    }
}
