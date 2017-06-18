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

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by ORamirez on 6/18/2017.
 */

public class InterestProfilerFragment extends Fragment
{
    private RecyclerView mIPRecyclerView;
    private static final String TAG = "IPFragment";

    public static InterestProfilerFragment newInstance()
    {
        return new InterestProfilerFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ip_list, container, false);

        mIPRecyclerView = (RecyclerView) v.findViewById(R.id.ipList_recyclerView);
        mIPRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            new IPFetcher().fetchItems();
            return null;
        }
    }
}
