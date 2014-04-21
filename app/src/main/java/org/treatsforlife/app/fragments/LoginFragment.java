package org.treatsforlife.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.treatsforlife.app.R;
import org.treatsforlife.app.animations.AnimationFactory;
import org.treatsforlife.app.entities.User;
import org.treatsforlife.app.events.LoginCompleteEvent;
import org.treatsforlife.app.infra.Logger;
import org.treatsforlife.app.providers.BusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {

    //Views
    @InjectView(R.id.viewSwitcher) ViewSwitcher mViewSwitcher;
    @InjectView(R.id.authButton) LoginButton mLoginButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    //Facebook Auth
    private UiLifecycleHelper uiHelper;
    Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    //Misc
    private boolean mIsGettingUserInfo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        mLoginButton.setFragment(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @OnClick({ R.id.btnFlip, R.id.btnBack })
    public void flipAnimation(Button button) {
        AnimationFactory.flipTransition(mViewSwitcher, AnimationFactory.FlipDirection.LEFT_RIGHT, 200l);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened() && !mIsGettingUserInfo) {
            mIsGettingUserInfo = true;
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser graphUser, Response response) {
                    User user = new User();
                    user.id = graphUser.getId();
                    user.fullName = graphUser.getName();
                    user.persist(getActivity());
                    BusProvider.getInstance().post(new LoginCompleteEvent());
                    Logger.l("Logged in...");
                    mIsGettingUserInfo = false;
                }
            }).executeAsync();
        } else if (state.isClosed()) {
            Logger.l("Logged out...");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
}