package org.master.upv.threads.calculator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.master.upv.threads.R;

public class CalculatorSHA1Fragment extends Fragment implements ResultCallback<String> {
    Sha1HashService mService;
    boolean mBound = false;

    private EditText toHash;
    private TextView hashResult;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            Sha1HashService.LocalBinder binder = (Sha1HashService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService= null;
        }
    };

    public CalculatorSHA1Fragment() {
        // Required empty public constructor
    }

    public static CalculatorSHA1Fragment newInstance() {
        return new CalculatorSHA1Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_calculator_sha1, container, false);

        Button queryButton = root.findViewById(R.id.hashIt);
        toHash = root.findViewById(R.id.text);
        hashResult = root.findViewById(R.id.hashResult);

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mService != null) {
                    mService.getSha1Digest(toHash.getText().toString(), CalculatorSHA1Fragment.this);
                }
            }
        });

        return root;
    }

    @Override
    public void onResult(String data) {
        hashResult.setText(data);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), Sha1HashService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            getContext().unbindService(mConnection);
            mBound = false;
        }
    }

}
