package com.appsforprogress.android.careerpath;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ORamirez on 6/18/2017.
 */

public class InterestProfilerFragment extends Fragment
{
    private RecyclerView mIPRecyclerView;
    private static final String TAG = "IPFragment";
    private List<Question> mQuestions = new ArrayList<>();


    public static InterestProfilerFragment newInstance()
    {
        return new InterestProfilerFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Connect to API, download XML, parse and display:
        new FetchItemsTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_ip_list, container, false);

        mIPRecyclerView = (RecyclerView) v.findViewById(R.id.ipList_recyclerView);
        mIPRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        return v;
    }

    private void setupAdapter()
    {
        if (isAdded())
        {
            mIPRecyclerView.setAdapter(new QuestionAdapter(mQuestions));
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<Question>>
    {
        @Override
        protected List<Question> doInBackground(Void... params)
        {
            return new IPFetcher().fetchItems();
        }

        // Runs on Main Thread: Safe place to update UI

        @Override
        protected void onPostExecute(List<Question> items)
        {
            mQuestions = items;

            // Allow adapter to handle RecyclerView
            setupAdapter();
        }
    }

    // ViewHolder for RecyclerView:
    private class QuestionHolder extends RecyclerView.ViewHolder
    {
        private TextView mQuestionTextView;

        public QuestionHolder(View itemView)
        {
            super(itemView);

            mQuestionTextView = (TextView) itemView;
        }

        public void bindQuestionItem(Question question)
        {
            mQuestionTextView.setText(question.getText());
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder>
    {
        private List<Question> mQuestionItems;

        public QuestionAdapter(List<Question> questionItems)
        {
            mQuestionItems = questionItems;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            TextView textView = new TextView(getActivity());
            return new QuestionHolder(textView);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position)
        {
            Question question = mQuestionItems.get(position);
            holder.bindQuestionItem(question);
        }

        @Override
        public int getItemCount()
        {
            return mQuestionItems.size();
        }
    }
}
