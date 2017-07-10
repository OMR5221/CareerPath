package com.appsforprogress.android.careerpath;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment
{
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    SharedPreferences pref;


    // For FaceBook Login:
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton mFBLoginButton;
    private List<FBLike> fbLikes = new ArrayList<FBLike>();
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;

    public static final String EXTRA_USER_ID = "com.appsforprogress.android.mycareerpath.user_id";
    public static final String EXTRA_FIRST_NAME = "com.appsforprogress.android.mycareerpath.first_name";
    public static final String EXTRA_LAST_NAME = "com.appsforprogress.android.mycareerpath.last_name";
    public static final String EXTRA_IMAGE_LINK = "com.appsforprogress.android.mycareerpath.profile_image";
    public static final String EXTRA_LOGIN_RESULT = "com.appsforprogress.android.mycareerpath.login_result";

    // Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }
        @Override
        public void onCancel() {        }
        @Override
        public void onError(FacebookException e) {      }
    };

    public LoginFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Facebook Login button:
        mFBLoginButton = (LoginButton) v.findViewById(R.id.fb_login_button);

        // mFBLoginButton.setReadPermissions("email");
        mFBLoginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        mFBLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                if (profile != null)
                {
                    facebook_id = profile.getId();
                    f_name = profile.getFirstName();
                    m_name = profile.getMiddleName();
                    l_name = profile.getLastName();
                    full_name = profile.getName();
                    profile_image = profile.getProfilePictureUri(400, 400).toString();
                }

                GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback()
                                {
                                    @Override
                                    public void onCompleted(
                                            final JSONObject object,
                                            GraphResponse response)
                                    {
                                        // Application code
                                        final JSONObject jsonObject = response.getJSONObject();
                                        String name = "";
                                        String email = "";
                                        String id = "";
                                        // convert Json object into Json array
                                        JSONArray posts;

                                        try
                                        {
                                            id = object.optString("id");
                                            name = jsonObject.getString("name");
                                            email =  jsonObject.getString("email");
                                            email_id=object.getString("email");
                                            gender=object.getString("gender");
                                            String profile_name=object.getString("name");
                                            long fb_id=object.getLong("id"); //use this for logout
                                            posts = jsonObject.getJSONObject("likes").optJSONArray("data");

                                            // LOOP through retrieved JSON posts:
                                            for (int i = 0; i < posts.length(); i++)
                                            {
                                                JSONObject post = posts.optJSONObject(i);
                                                String likeId = post.optString("id");
                                                String likeCategory = post.optString("category");
                                                String likeName = post.optString("name");
                                                int likeCount = post.optInt("likes");

                                                // print id, page name and number of like of facebook page
                                                Log.e("id: ", likeId + " (name: " + likeName + " , category: "+ likeCategory + " likes count - " + likeCount);

                                                // Create Like Objects
                                                FBLike fbLike = new FBLike();
                                                fbLike.setId(likeId);
                                                fbLike.setCategory(likeCategory);
                                                fbLike.setName(likeName);

                                                // Add each like to a List
                                                fbLikes.add(fbLike);
                                            }

                                            /*
                                            Intent i = new Intent(getActivity(), UserProfileActivity.class);

                                            i.putExtra("type","facebook");
                                            i.putExtra("facebook_id",id);
                                            i.putExtra("f_name",f_name);
                                            i.putExtra("m_name",m_name);
                                            i.putExtra("l_name",l_name);
                                            i.putExtra("full_name",full_name);
                                            i.putExtra("profile_image",profile_image);
                                            i.putExtra("email_id",email_id);
                                            i.putExtra("gender",gender);

                                            Toast.makeText(getApplicationContext(), "Logged in...", Toast.LENGTH_SHORT).show();
                                            */

                                            Intent intent = UserProfileActivity.newIntent(getActivity(), f_name, l_name, profile_image);
                                            startActivity(intent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );

                /*
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email, posts");
                request.setParameters(parameters);
                */
                request.executeAsync();
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException e) {}
        });
        // loginButton.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {}
        };


        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile)
            {
                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop FB Login:
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent)
    {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }


    private void nextActivity(Profile profile)
    {
        if (profile != null)
        {
            String firstName = profile.getFirstName();
            String lastName =  profile.getLastName();
            String profileImage = profile.getProfilePictureUri(200,200).toString();

            Intent intent = UserProfileActivity.newIntent(getActivity(), firstName, lastName, profileImage);
            startActivity(intent);
        }
    }

    public static LoginFragment newInstance()
    {
        // return new UserProfileFragment();

        // UUID userId = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);

        return new LoginFragment();
    }





    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("FacebookFragment", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
            String title ="ERROR";
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FacebookFragment", "Success!");
            if (result.getPostId() != null) {
                String title = "SUCCESS";
                String id = result.getPostId();
                String alertMessage = "POSTED";
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton("OK", null)
                    .show();
        }
    };


}

