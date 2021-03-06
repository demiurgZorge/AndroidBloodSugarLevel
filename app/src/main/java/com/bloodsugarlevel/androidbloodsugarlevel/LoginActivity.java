package com.bloodsugarlevel.androidbloodsugarlevel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.LoginDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.RegisterUserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.AuthRequest;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.livedata.UserViewModel;
import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.UserDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static final String LOGIN_VOLEY_TAG = "LOGIN_VOLEY_TAG";
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    RequestQueue mRequestQueue;
    private UserViewModel userViewModel;
    Observer<User> mUserRegisterObserver;
    Observer<User> mUserLoginObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRequestQueue = MyApplication.getInstance().getRequestQueue();
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mUserRegisterObserver = new Observer<User> () {

            @Override
            public void onChanged(@Nullable User user) {
                if(user != null) {
                    MyApplication.getInstance().setLoggedUserName(user.login);
                    startMainActivity();
                }else{
                    AlertDialogManager.showAlertDialog(LoginActivity.this, "Error", "User not registered!");
                    showProgress(false);
                }
                return;
            }
        };

        mUserLoginObserver  = new Observer<User> () {

            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    MyApplication.getInstance().setLoggedUserName(user.login);
                    startMainActivity();
                } else {
                    AlertDialogManager.showAlertDialog(LoginActivity.this, "Error", "User not found!");
                    showProgress(false);
                }
                return;
            }
        };
        MutableLiveData<User> userModelData = new MutableLiveData<User>();
        userModelData.removeObservers(this);
        userModelData.observe(this, mUserRegisterObserver);
        userViewModel.setUserData(userModelData);


        if (MyApplication.getInstance().isOnlineAndAuthenticated()) {
            loginWithToken();
            return;
        }
        if (MyApplication.getInstance().isOfflineAndUserRegistered()) {
            startMainActivity();
            return;
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    private void loginWithToken() {
        AuthRequest jsonObjectRequest = HttpRequestFactory.loginWithTokenUserRequest(this,
                new IUiUpdateEntityListener<UserDto>() {
                    @Override
                    public void onResponse(UserDto userDto) {
                        startMainActivity();
                    }
                },
                MyApplication.getInstance().getToken(),
                LOGIN_VOLEY_TAG);
        mRequestQueue.add(jsonObjectRequest);
        showProgress(true);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {

        if (!checkEtitTexts()) {
            showProgress(true);
        }


        LoginDto userLoginDto = new LoginDto();
        userLoginDto.login = mEmailView.getText().toString();
        userLoginDto.password = mPasswordView.getText().toString();
        if (MyApplication.getInstance().isInternetAllow()) {
            JsonObjectRequest jsonObjectRequest = HttpRequestFactory.loginWithPasswordUserRequest(this,
                    new IUiUpdateEntityListener<UserDto>() {
                        @Override
                        public void onResponse(UserDto userDto) {
                            MyApplication.getInstance().getLoggedUserToken(userDto.authToken);
                            MyApplication.getInstance().setLoggedUserName(userDto.name);
                            startMainActivity();
                        }
                    },
                    userLoginDto,
                    LOGIN_VOLEY_TAG);
            mRequestQueue.add(jsonObjectRequest);
        } else {

            userViewModel.getUser().removeObservers(this);
            userViewModel.getUser().observe(this, mUserLoginObserver);
            userViewModel.setLogin(userLoginDto.login);
        }
    }

    private boolean checkEtitTexts() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }

    public void attemptRegister() {
        if (!checkEtitTexts()) {
            showProgress(true);
        }
        final RegisterUserDto userRegisterDto = new RegisterUserDto();
        userRegisterDto.login = mEmailView.getText().toString();
        userRegisterDto.password = mPasswordView.getText().toString();
        userRegisterDto.name = mEmailView.getText().toString();

        if (MyApplication.getInstance().isInternetAllow()) {
            onlineRegisterUser(userRegisterDto);
        } else {
            userViewModel.getUser().removeObservers(this);
            userViewModel.getUser().observe(this, mUserRegisterObserver);
            userViewModel.tryCreateUser(userRegisterDto.login, userRegisterDto.password);
        }
    }

    private void onlineRegisterUser(final RegisterUserDto userRegisterDto) {
        JsonObjectRequest jsonObjectRequest = HttpRequestFactory.registerUserRequest(this,
                new IUiUpdateEntityListener<UserDto>() {
                    @Override
                    public void onResponse(UserDto userDto) {
                        MyApplication.getInstance().setLoggedUser(userDto.authToken, userDto.name);
                        startMainActivity();
                    }
                },
                userRegisterDto,
                LOGIN_VOLEY_TAG);
        mRequestQueue.add(jsonObjectRequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(LOGIN_VOLEY_TAG);
        }
    }
}

