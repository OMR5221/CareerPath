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
    private EditText mFirstNameEdit;
    private EditText mLastNameEdit;

    // Facebook Login View:
    private TextView mUserName ;
    private TextView mGender;
    private TextView mLocation;
    private ImageView mProfilePicture;
    private FloatingActionButton postFab;
    private RecyclerView mFBLikeRecyclerView;

    public static UserProfileFragment newInstance() //UUID userId)
    {
        Bundle args = new Bundle();
        // args.putSerializable(ARG_USER_ID, userId);

        UserProfileFragment fragment = new UserProfileFragment();
        // fragment.setArguments(args);

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

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken)
            {}
        };

        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile)
            {
                this.stopTracking();
                Profile.setCurrentProfile(newProfile);

                Profile profile = Profile.getCurrentProfile();

                // Get profile elements:
                String firstName = profile.getFirstName();
                String lastName = profile.getLastName();
                String imageUrl = profile.getProfilePictureUri(200,200).toString();

                Toast.makeText(getActivity().getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                // MAKE OTHER ELEMENTS VISIBLE AND SET VALUES:
                mUserName.setText("" + firstName + " " + lastName);
                mUserName.setVisibility(View.VISIBLE);

                new DownloadImage(mProfilePicture).execute(imageUrl);
                mProfilePicture.setVisibility(View.VISIBLE);

                shareDialog = new ShareDialog(getActivity());

                setupAdapter();

                mFBLikeRecyclerView.setVisibility(View.VISIBLE);

                postFab.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ShareLinkContent content = new ShareLinkContent.Builder().build();
                        shareDialog.show(content);
                    }
                });

                postFab.setVisibility(View.VISIBLE);

                // Check first 5 likes:

            }
        };

        profileTracker.startTracking();
        accessTokenTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mUserName = (TextView) view.findViewById(R.id.fb_username);
        mUserName.setVisibility(View.INVISIBLE);
        mProfilePicture = (ImageView) view.findViewById(R.id.profileImage);
        mProfilePicture.setVisibility(View.INVISIBLE);

        postFab = (FloatingActionButton) view.findViewById(R.id.fab);
        postFab.setVisibility(View.INVISIBLE);

        mFBLikeRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_like_gallery_recycler_view);
        // Set up row of 3 elements
        mFBLikeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mFBLikeRecyclerView.setVisibility(View.INVISIBLE);

        mCallback = new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                mFBLikeItems = getLikedPageInfo(loginResult);
                Toast.makeText(getActivity().getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };

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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        // TODO: get some more permissions from the user
        loginButton.setReadPermissions(Arrays.asList(
            "public_profile", "email", "user_birthday", "user_friends", "user_likes"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            // Login successful:
            // AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            /*
            // Get profile elements:
            String firstName = profile.getFirstName();
            String lastName = profile.getFirstName();
            String imageUrl = profile.getProfilePictureUri(200,200).toString();

            Toast.makeText(getActivity().getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

            // MAKE OTHER ELEMENTS VISIBLE AND SET VALUES:
            mUserName.setText("" + firstName + " " + lastName);
            mUserName.setVisibility(View.VISIBLE);

            new DownloadImage(mProfilePicture).execute(imageUrl);
            mProfilePicture.setVisibility(View.VISIBLE);

            shareDialog = new ShareDialog(getActivity());

            postFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ShareLinkContent content = new ShareLinkContent.Builder().build();
                    shareDialog.show(content);
                }
            });

            postFab.setVisibility(View.VISIBLE);
            */

        }

        @Override
        public void onCancel()
        {
            //TODO, implement onCancel
        }

        @Override
        public void onError(FacebookException e)
        {
            //TODO, implement onError inorder to handle the errors
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbackManager.onActivityResult(requestCode, resultCode, data))
        {
            return;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        profileTracker.startTracking();
        accessTokenTracker.startTracking();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

/*
 * To get the Facebook page which is liked by user's through creating a new request.
 * When the request is completed, a callback is called to handle the success condition.
*/
    protected List<FBLike> getLikedPageInfo(LoginResult loginResult)
    {
        final List<FBLike> fbLikes = new ArrayList<FBLike>();

        // Create a FB Like Object to load likes into:
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response)
                    {

                        try
                        {
                            // convert Json object into Json array
                            JSONArray posts = json_object.getJSONObject("likes").optJSONArray("data");

                            // LOOP through retrieved JSON posts:
                            for (int i = 0; i < posts.length(); i++)
                            {
                                JSONObject post = posts.optJSONObject(i);
                                String id = post.optString("id");
                                String category = post.optString("category");
                                String name = post.optString("name");
                                int count = post.optInt("likes");
                                // print id, page name and number of like of facebook page
                                Log.e("id: ", id + " (name: " + name + " , category: "+ category + " likes count - " + count);

                                FBLike fbLike = new FBLike();
                                fbLike.setId(id);
                                fbLike.setCategory(category);
                                fbLike.setName(name);

                                // Add each like to a List
                                fbLikes.add(fbLike);

                            }
                        }
                        catch(Exception e) {}
                    }
                });

        Bundle permission_param = new Bundle();
        // add the field to get the details of liked pages
        permission_param.putString("fields", "likes{id,category,name,location,likes}");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

        return fbLikes;
    }

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
