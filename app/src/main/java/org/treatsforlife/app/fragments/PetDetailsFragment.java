package org.treatsforlife.app.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.treatsforlife.app.R;
import org.treatsforlife.app.entities.Pet;
import org.treatsforlife.app.infra.Globals;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PetDetailsFragment extends BaseFragment {

    @InjectView(R.id.progressBar) ProgressBar mProgress;
    @InjectView(R.id.tvPetDetailsName) TextView mTVPetName;

    Pet mPet;
    Future<Pet> mIsLoadingPet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            if (getArguments().containsKey(Globals.EXTRA_PET_ID)) {
                String petID = getArguments().getString(Globals.EXTRA_PET_ID);
                if (!TextUtils.isEmpty(petID))
                    getPetDetails(petID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_details, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    private void getPetDetails(String petID) {
        if (mIsLoadingPet != null && !mIsLoadingPet.isDone() && !mIsLoadingPet.isCancelled())
            return;

        mIsLoadingPet = Ion.with(getActivity())
                .load(Globals.API_PET_URL + petID)
                .as(new TypeToken<Pet>(){})
                .setCallback(new FutureCallback<Pet>() {
                    @Override
                    public void onCompleted(Exception e, Pet pet) {
                        try {
                            if (e != null)
                                throw e;
                            mPet = pet;
                            initUI();
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "ERROR " + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initUI() {
        if (null != mProgress)
            mProgress.setVisibility(View.GONE);

        if (null != mPet) {
            if (!TextUtils.isEmpty(mPet.name))
                mTVPetName.setText(mPet.name);
        }
        else
            Toast.makeText(getActivity(), "Error loading pet!", Toast.LENGTH_SHORT).show();
    }
}
