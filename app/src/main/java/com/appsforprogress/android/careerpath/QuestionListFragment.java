package com.appsforprogress.android.careerpath;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

/**
 * Created by ORamirez on 6/14/2017.
 */

public class QuestionListFragment extends Fragment
{
    private RecyclerView mQuestionListRecyclerView;
    private QuestionAdapter mQuestionAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the Question List Fragment Layout:
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        // Set up the RecyclerView
        mQuestionListRecyclerView = (RecyclerView) view.findViewById(R.id.questionList_recyclerView);
        mQuestionListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Connect Adapter to recyclerView
        updateUI();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    // Set up View Holder object which is controlled by the Adapter which then responds to the RecyclerView:
    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Question mQuestion;
        private TextView mQuestionTextView;
        private CheckBox mIsAnsweredCheckBox;

        public QuestionHolder(View itemView)
        {
            super(itemView);

            // Enable Clicking Holder
            itemView.setOnClickListener(this);

            mQuestionTextView = (TextView) itemView.findViewById(R.id.list_item_question_text_view);
            mIsAnsweredCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_isAnswered_checkBox);

        }

        // Add logic to bind data from DB to ViewHolder from Adapter:
        public void bindQuestion(Question question)
        {
            mQuestion = question;
            mQuestionTextView.setText(mQuestion.getText());
            mIsAnsweredCheckBox.setChecked(mQuestion.getAnswered());
        }

        @Override
        public void onClick(View v)
        {
            // Call the Detail Activities' Intent creation function:
            Intent intent = QuestionDetailActivity.detailQuestionIntent(getActivity(), mQuestion.getId());
            startActivity(intent);
        }
    }

    // Set up Adapter to be manager of viewHolders in response to recycler view scrolling:
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder>
    {
        private List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions)
        {
            mQuestions = questions;
        }

        // Called by RecyclerView when a new View is needed to display an item:
        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a default view and wrap into a ViewHolder: Will be empty
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            View view = layoutInflater.inflate(R.layout.list_item_question, parent, false);

            return new QuestionHolder(view);
        }

        // Insert Question data into a QuestionHolder:
        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            // Send question object to ViewHolder to bind to View:
            holder.bindQuestion(question);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }

        public void setQuestions(List<Question> questions)
        {
            mQuestions = questions;
        }
    }

    private void updateUI()
    {
        // Get a Quiz instance if exists
        Quiz quiz = Quiz.get(getActivity());

        List<Question> questions = quiz.getQuestions();

        // Set up Question Adapter by supplying List of Question objects to manage:
        if (mQuestionAdapter == null)
        {
            mQuestionAdapter = new QuestionAdapter(questions);
            // Set RecyclerView to communicate with the Adapter
            mQuestionListRecyclerView.setAdapter(mQuestionAdapter);
        }
        else {
            // Update questions
            mQuestionAdapter.setQuestions(questions);
            mQuestionAdapter.notifyDataSetChanged();
        }

    }
}
