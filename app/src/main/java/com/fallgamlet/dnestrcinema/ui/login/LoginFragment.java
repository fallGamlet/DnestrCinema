package com.fallgamlet.dnestrcinema.ui.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.utils.ViewUtils;

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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.error)
    TextView errorView;
    View rootView;

    TextView.OnEditorActionListener actionListener;


    public LoginFragment() {
        MvpLoginPresenter presenter = AppFacade.Companion.getInstance().getPresenterFactory().createLoginPresenter();
        setPresenter(presenter);
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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        rootView.setOnClickListener(this);

        loginButton.setOnClickListener(this);

        loginEditText.setOnEditorActionListener(getOnEditorActionListener());

        loginEditText.addTextChangedListener(new BaseTextWatcher(){
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginChanged(charSequence.toString().trim());
            }
        });

        passwordEditText.setOnEditorActionListener(getOnEditorActionListener());

        passwordEditText.addTextChangedListener(new BaseTextWatcher(){
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordChanged(charSequence.toString().trim());
            }
        });

    }

    private TextView.OnEditorActionListener getOnEditorActionListener() {
        if (actionListener == null) {
            actionListener = new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        getPresenter().onLogin();
                        return true;
                    }
                    return false;
                }
            };
        }

        return actionListener;
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
            getPresenter().onSave(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (isPresenterExist()) {
            getPresenter().onRestore(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        getPresenter().unbindView();

        ViewUtils.INSTANCE.hideKeyboard(getContext(), loginEditText);
        ViewUtils.INSTANCE.hideKeyboard(getContext(), passwordEditText);

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().bindView(this);
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
    public void setLoginButtonEnabled(boolean enabled) {
        if (this.loginButton != null) {
            this.loginButton.setEnabled(enabled);
        }
    }

    @Override
    public void setLoginButtonVisible(boolean v) {
        if (this.loginButton != null) {
            this.loginButton.setVisibility(v ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setLoading(boolean v) {
        progressBar.setVisibility(v? View.VISIBLE: View.GONE);
        setLoginButtonVisible(!v);
    }

    @Override
    public void setErrorVisible(boolean v) {
        errorView.setVisibility(v? View.VISIBLE: View.GONE);
    }
}
