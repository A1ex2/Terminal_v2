package ua.org.algoritm.terminal.ConnectTo1c;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class UIManagerNew {
    View mView;

    public UIManagerNew(View view) {
        mView = view;
    }

    public void showToast(String message){
        Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
