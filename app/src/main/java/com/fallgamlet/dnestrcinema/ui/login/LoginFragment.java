package com.fallgamlet.dnestrcinema.ui.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.mvp.views.MvpBaseFragment;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment
        extends
            Fragments.MvpLoginViewFragment
        implements
            View.OnClickListener
{

    @BindView(R.id.login)
    EditText loginEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    View loginButton;
    View rootView;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        initListeners();

        return rootView;
    }

    private void initListeners() {
        rootView.setOnClickListener(this);

        loginButton.setOnClickListener(this);

        loginEditText.addTextChangedListener(new BaseTextWatcher(){
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginChanged(charSequence);
            }
        });

        passwordEditText.addTextChangedListener(new BaseTextWatcher(){
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordChanged(charSequence);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }

        if (view == loginButton && isPresenterExist()) {
            getPresenter().onLogin();
        }

    }

    private void loginChanged(CharSequence value) {
        if (isPresenterExist()) {
            getPresenter().onLoginChanged(value.toString());
        }
    }

    private void passwordChanged(CharSequence value) {
        if (isPresenterExist()) {
            getPresenter().onPasswordChanged(value.toString());
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isPresenterExist()) {
            getPresenter().onPause(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (isPresenterExist()) {
            getPresenter().onResume(savedInstanceState);
        }
    }


    @Override
    public void setLogin(CharSequence value) {
        if (this.loginEditText != null) {
            this.loginEditText.setText(value);
        }
    }

    @Override
    public void setPassword(CharSequence value) {
        if (this.passwordEditText != null) {
            this.passwordEditText.setText(value);
        }
    }

    @Override
    public void setLoginEnabled(boolean enabled) {
        if (this.loginEditText != null) {
            this.loginEditText.setEnabled(enabled);
        }
    }

    @Override
    public void setPasswordEnabled(boolean enabled) {
        if (this.passwordEditText != null) {
            this.passwordEditText.setEnabled(enabled);
        }
    }

    @Override
    public void setLoginButtonEnabled(boolean enabled) {
        if (this.loginButton != null) {
            this.loginButton.setEnabled(enabled);
        }
    }

    @Override
    public void setLoginVisible(boolean v) {
        if (this.loginEditText != null) {
            this.loginEditText.setVisibility(v ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setPasswordVisible(boolean v) {
        if (this.passwordEditText != null) {
            this.passwordEditText.setVisibility(v ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setLoginButtonVisible(boolean v) {
        if (this.loginButton != null) {
            this.loginButton.setVisibility(v ? View.VISIBLE : View.GONE);
        }
    }
}
