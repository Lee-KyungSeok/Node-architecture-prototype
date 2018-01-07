package com.example.feature_onboarding;

import android.os.Parcelable;

import com.example.feature_onboarding.di.OnboardingScope;
import com.example.feature_onboarding.login.LoginBuilder;
import com.example.feature_onboarding.signup.SignUpBuilder;
import com.levnovikov.system_base.Router;
import com.levnovikov.system_base.state.NodeState;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

/**
 * Created by lev.novikov
 * Date: 22/11/17.
 */

@OnboardingScope
public class OnboardingRouter extends Router {

    private final LoginBuilder loginBuilder;
    private final SignUpBuilder signUpBuilder;

    @Inject
    OnboardingRouter(LoginBuilder loginBuilder, SignUpBuilder signUpBuilder) {
        this.loginBuilder = loginBuilder;
        this.signUpBuilder = signUpBuilder;
    }

    void attachLogInScreen() {
        attachNode(loginBuilder);
    }

    void removeAll() {
        detachNode(loginBuilder);
        detachNode(signUpBuilder);
    }

    void attachSignUpScreen() {
        attachNode(signUpBuilder);
    }

    @Override
    protected void destroyNode() {
        removeAll();
    }

    @Override
    public NodeState getNodeState(@Nullable Parcelable stateData) { //TODO remove stub
        return NodeState.create(this.getClass(), null);
    }

    @Override
    public void setState(NodeState state) {
        //TODO remove stub
    }
}
