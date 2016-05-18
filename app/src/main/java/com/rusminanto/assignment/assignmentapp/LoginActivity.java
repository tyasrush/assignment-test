package com.rusminanto.assignment.assignmentapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rusminanto.assignment.assignmentapp.util.NetworkUtil;
import com.rusminanto.assignment.assignmentapp.util.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via username and password.
 */
public class LoginActivity extends AppCompatActivity implements
        OnClickListener,
        NetworkUtil.OnLoadDataFinishedListener {

    private NetworkUtil networkUtil;
    private UserUtil userUtil;

    private EditText usernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userUtil = new UserUtil(this);

        networkUtil = new NetworkUtil();
        networkUtil.setOnLoadDataFinishedListener(this);

        usernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * checking if user is logged in
         */
        if (userUtil.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, PromoActivity.class));
            finish();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        usernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (networkUtil.isInternetConnected(this)) {
                showProgress(true);
                try {
                    networkUtil.getData(new String[]{BuildConfig.login_api, username, password}, NetworkUtil.HttpMethod.POST, NetworkUtil.Case.LOGIN);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

    private void resetForm() {
        usernameView.setText(null);
        mPasswordView.setText(null);
        usernameView.requestFocus();
    }

    @Override
    public void onClick(View v) {
        attemptLogin();
    }

    @Override
    public void onLoadDataFinished(final String result) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                try {
                    if (result.contains("result")) {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean isCorrectUser = (boolean) jsonObject.get("result");
                        if (isCorrectUser) {
                            userUtil.setUser(usernameView.getText().toString().trim(), mPasswordView.getText().toString().trim());
                            startActivity(new Intent(LoginActivity.this, PromoActivity.class));
                            finish();
                        } else {
                            resetForm();
                            Toast.makeText(LoginActivity.this, getString(R.string.error_account_not_found), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        resetForm();
                        Toast.makeText(LoginActivity.this, getString(R.string.error_account_not_found), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

