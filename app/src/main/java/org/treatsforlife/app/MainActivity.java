package org.treatsforlife.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import com.squareup.otto.Subscribe;

import org.treatsforlife.app.events.LoginCompleteEvent;
import org.treatsforlife.app.fragments.LoginFragment;
import org.treatsforlife.app.providers.BusProvider;

public class MainActivity extends ActionBarActivity {

    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            mLoginFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void loginComplete(LoginCompleteEvent event) {
        Toast.makeText(this, "Login Successful!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}