package org.treatsforlife.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import com.squareup.otto.Subscribe;

import org.treatsforlife.app.entities.User;
import org.treatsforlife.app.events.LoginCompleteEvent;
import org.treatsforlife.app.fragments.LoginFragment;
import org.treatsforlife.app.providers.BusProvider;

public class SplashActivity extends FragmentActivity {
    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        User user = new User(this);
        if (null != user && !TextUtils.isEmpty(user.id) && !TextUtils.isEmpty(user.fullName))
            loginComplete(null);
        else {
            Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
            Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

            if (savedInstanceState == null) {
                // Add the fragment on initial activity setup
                mLoginFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, mLoginFragment)
                        .commit();
            } else {
                // Or set the fragment from restored state info
                mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.splash_content);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void loginComplete(LoginCompleteEvent event) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}