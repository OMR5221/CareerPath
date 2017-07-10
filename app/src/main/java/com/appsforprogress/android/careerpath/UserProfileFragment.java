package com.appsforprogress.android.careerpath;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oswald on 3/5/2016.
 */
public class UserProfileFragment extends Fragment
{
    private ShareDialog shareDialog;
    private User mUser;
    private String mFirstName;
    private String mLastName;
    private String mProfilePicURL;

    // Facebook Login View:
    private TextView mUserName;
    private TextView mGender;
    private TextView mLocation;
    private ImageView mProfilePicture;
    private FloatingActionButton postFab;
    private RecyclerView mFBLikeRecyclerView;


    public static final String EXTRA_USER_ID = "com.appsforprogress.android.mycareerpath.user_id";
    public static final String EXTRA_FIRST_NAME = "com.appsforprogress.android.mycareerpath.first_name";
    public static final String EXTRA_LAST_NAME = "com.appsforprogress.android.mycareerpath.last_name";
    public static final String EXTRA_IMAGE_LINK = "com.appsforprogress.android.mycareerpath.profile_image";
    public static final String EXTRA_LOGIN_RESULT = "com.appsforprogress.android.mycareerpath.login_result";

    public static UserProfileFragment newInstance(String firstName, String lastName, String profileImg)
    {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_FIRST_NAME, firstName);
        args.putSerializable(EXTRA_LAST_NAME, lastName);
        args.putSerializable(EXTRA_IMAGE_LINK, profileImg);

        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private List<FBLike> mFBLikeItems = new ArrayList<>();

    public UserProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFirstName = (String) getActivity().getIntent()
                .getSerializableExtra(UserProfileActivity.EXTRA_FIRST_NAME);
        mLastName = (String) getActivity().getIntent()
                .getSerializableExtra(UserProfileActivity.EXTRA_LAST_NAME);
        mProfilePicURL = (String) getActivity().getIntent()
                .getSerializableExtra(UserProfileActivity.EXTRA_IMAGE_LINK);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mUserName = (TextView) view.findViewById(R.id.fb_username);
        mUserName.setText("" + mFirstName + " " + mLastName);
        mUserName.setVisibility(View.VISIBLE);

        mProfilePicture = (ImageView) view.findViewById(R.id.profileImage);
        new DownloadImage(mProfilePicture).execute(mProfilePicURL);
        mProfilePicture.setVisibility(View.VISIBLE);

        /*
        postFab = (FloatingActionButton) view.findViewById(R.id.fab);
        postFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });
        postFab.setVisibility(View.INVISIBLE);
        */

        mFBLikeRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_like_gallery_recycler_view);
        // Set up row of 3 elements
        mFBLikeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mFBLikeRecyclerView.setVisibility(View.VISIBLE);

        //shareDialog = new ShareDialog(getActivity());

        setupAdapter();

        return view;
    }


    private void setupAdapter()
    {
        if (isAdded())
        {
            mFBLikeRecyclerView.setAdapter(new FBLikeAdapter(mFBLikeItems));
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

/*
 * To get the Facebook page which is liked by user's through creating a new request.
 * When the request is completed, a callback is called to handle the success condition.
*/

    // Create object to hold each FBLike entry to be displayed in RecyclerView
    private class FBLikeHolder extends RecyclerView.ViewHolder
    {
        private TextView mCategoryTextView;
        private TextView mNameTextView;

        public FBLikeHolder(View fbLikeView)
        {
            super(fbLikeView);

            mCategoryTextView = (TextView) fbLikeView.findViewById(R.id.fragment_fblike_category);
            mNameTextView = (TextView) fbLikeView.findViewById(R.id.fragment_fblike_name);
        }

        public void bindLikeItem(FBLike fbLikeItem)
        {
            mNameTextView.setText(fbLikeItem.getName().toString());
            mCategoryTextView.setText(fbLikeItem.getCategory().toString());
        }
    }

    // Create adapter to handle FBLike refreshing
    private class FBLikeAdapter extends RecyclerView.Adapter<FBLikeHolder>
    {
        private List<FBLike> mFBLikes;

        public FBLikeAdapter(List<FBLike> fbLikes)
        {
            mFBLikes = fbLikes;
        }

        @Override
        public FBLikeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View fbLikeView = inflater.inflate(R.layout.fblike_item, parent, false);
            return new FBLikeHolder(fbLikeView);
        }

        // Bind the FBLike Object to the Holder managed by this Adapter
        @Override
        public void onBindViewHolder(FBLikeHolder fbLikeHolder, int position)
        {
            FBLike fbLike = mFBLikes.get(position);
            fbLikeHolder.bindLikeItem(fbLike);
        }

        @Override
        public int getItemCount()
        {
            return mFBLikes.size();
        }
    }
}
