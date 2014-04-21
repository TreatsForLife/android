package org.treatsforlife.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.otto.Subscribe;

import org.treatsforlife.app.R;
import org.treatsforlife.app.adapters.PetListAdapter;
import org.treatsforlife.app.entities.Pet;
import org.treatsforlife.app.events.PetListRefreshRequestedEvent;
import org.treatsforlife.app.infra.Globals;
import org.treatsforlife.app.providers.BusProvider;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PetListFragment extends Fragment {

    Future<List<Pet>> mIsLoadingPets;
    @InjectView(R.id.progressBar) ProgressBar mProgress;
    @InjectView(R.id.lvPets) ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);
        ButterKnife.inject(this, view);
        loadPets();
        return view;
    }

    void loadPets() {
        if (mIsLoadingPets != null && !mIsLoadingPets.isDone() && !mIsLoadingPets.isCancelled())
            return;

        mIsLoadingPets = Ion.with(getActivity())
                .load(Globals.API_BASE_URL)
                .as(new TypeToken<List<Pet>>(){})
                .setCallback(new FutureCallback<List<Pet>>() {
                    @Override
                    public void onCompleted(Exception e, List<Pet> pets) {
                        try {
                            if (e != null)
                                throw e;
                            PetListAdapter adapter = new PetListAdapter(getActivity(), pets);
                            mListView.setAdapter(adapter);
                            mProgress.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "ERROR " + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Subscribe
    public void onPetListRefreshRequested(PetListRefreshRequestedEvent event) {
        if (null != mListView)
            mListView.setAdapter(null);
        if (null != mProgress)
            mProgress.setVisibility(View.VISIBLE);
        loadPets();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
