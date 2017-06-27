package com.appsforprogress.android.careerpath;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by ORamirez on 5/29/2017.
 */

public class QuestionDetailFragment extends Fragment
{
    // Question object to display in Fragment:
    private Question mQuestion;
    private TextView mQuestionText;
    private RadioGroup mAnswerRG;

    // Hash of arguments to fragment:
    private static final String ARG_QUESTION_ID = "question_id";
    private HashMap scoreMap = new HashMap();

    // Handles call from List to create a new Fragment Instance:
    public static QuestionDetailFragment newInstance(UUID questionId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_ID, questionId);

        QuestionDetailFragment fragment = new QuestionDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    // Configure Fragment:
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get Question ID from Hash of arguments:
        UUID questionId = (UUID) getArguments().getSerializable(ARG_QUESTION_ID);

        // Use our Activity to communicate with the Quiz Object holding the Questions created from DB data
        mQuestion = Quiz.get(getActivity()).getQuestion(questionId);
    }

    @Override
    // Refreshes the Detail Fragment with the Question selected from Question Listing
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Configure View for Fragment:
        View v = inflater.inflate(R.layout.fragment_question_detail, container, false);

        // Setup TextView for Question text:
        mQuestionText = (TextView) v.findViewById(R.id.question_detail_text);

        // Set Question Text data
        mQuestionText.setText(mQuestion.getText());

        // Get Radio Group for possible answers
        mAnswerRG = (RadioGroup) v.findViewById(R.id.question_answer_group);

        // If the question has been answered show the Radio Selection Button as selected:
        if (mQuestion.getAnswered())
        {
            Integer questionScore = mQuestion.getScore();

            // Need a hash for score to index:
            Integer sIndex = 5 - questionScore;

            ((RadioButton) mAnswerRG.getChildAt(sIndex)).setChecked(true);
        }

        // Listen for Radio Button selection:
        mAnswerRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // Logic when value changed:
                switch (checkedId) {
                    case R.id.strongly_agree_button:
                        mQuestion.setAnswered(TRUE);
                        mQuestion.setScore(5);
                        break;
                    case R.id.agree_button:
                        mQuestion.setAnswered(TRUE);
                        mQuestion.setScore(4);
                        break;
                    case R.id.neutral_button:
                        mQuestion.setAnswered(TRUE);
                        mQuestion.setScore(3);
                        break;
                    case R.id.disagree_button:
                        mQuestion.setAnswered(TRUE);
                        mQuestion.setScore(2);
                        break;
                    case R.id.strongly_disagree_button:
                        mQuestion.setAnswered(TRUE);
                        mQuestion.setScore(1);
                        break;
                    default:
                        mQuestion.setAnswered(FALSE);
                        mQuestion.setScore(0);
                        break;
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
