package org.treatsforlife.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.Session;
import com.koushikdutta.ion.Ion;
import com.squareup.otto.Subscribe;

import org.treatsforlife.app.entities.User;
import org.treatsforlife.app.events.PetListRefreshRequestedEvent;
import org.treatsforlife.app.events.PetListRowClickedEvent;
import org.treatsforlife.app.fragments.PetDetailsFragment;
import org.treatsforlife.app.fragments.PetListFragment;
import org.treatsforlife.app.infra.Globals;
import org.treatsforlife.app.providers.BusProvider;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Ion.getDefault(MainActivity.this).configure().setLogging("TFL Ion", Log.DEBUG);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new PetListFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_refresh:
                BusProvider.getInstance().post(new PetListRefreshRequestedEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void displayPetDetailsFragment(PetListRowClickedEvent event) {
        PetDetailsFragment petDetailsFragment = new PetDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Globals.EXTRA_PET_ID, event.petID);
        petDetailsFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, petDetailsFragment).addToBackStack(null).commit();
    }

    public void logout() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(this);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
        Session.setActiveSession(null);
        User.reset(this);
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}