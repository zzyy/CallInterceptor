package com.zy.callinterceptor.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Simon on 2015/1/29.
 */
public class BaseDialogFragment extends DialogFragment {

    protected Listener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null){
            mListener.onDismiss(dialog);
        }
    }

    public void setListener( Listener listener){
        this.mListener = listener;
    }

    public interface Listener{
        void onDismiss(DialogInterface dialog);
    }
}
