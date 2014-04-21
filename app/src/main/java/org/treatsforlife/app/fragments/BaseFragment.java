package org.treatsforlife.app.fragments;

import android.support.v4.app.Fragment;

import org.treatsforlife.app.providers.BusProvider;

public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().register(this);
    }
}
