package com.app.motiv.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.motiv.utils.MyProgressDialog;

/**
 * Created by 123 on 31-Jan-18.
 */

public abstract class BaseFragment extends Fragment {

    protected void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void switchFragment(int containerResourceId, Fragment fragment) {

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 1);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerResourceId, fragment);

        fragmentTransaction.addToBackStack(backStateName);

        fragmentTransaction.commit();
    }

    public void showProgress() {
        MyProgressDialog.show(getActivity());
    }

    public void hideProgress() {
        MyProgressDialog.hide();
    }
}
