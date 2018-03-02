package org.master.upv.threads.chronometer;

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
import android.widget.TextView;

import org.master.upv.threads.R;

public class ChronometerFragment extends Fragment {
    BoundService mBoundService;
    boolean mServiceBound = false;

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
        @Override public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    public ChronometerFragment() {
        // Required empty public constructor
    }

    public static ChronometerFragment newInstance() {
        return new ChronometerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chronometer, container, false);

        final TextView timestampText = root.findViewById(R.id.timestamptext);
        Button btnPrintTimeStamp = root.findViewById(R.id.btnPrintTimeStamp);
        Button btnStopService = root.findViewById(R.id.btnStopService);
        btnPrintTimeStamp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mServiceBound) {
                    timestampText.setText(mBoundService.getTimestamp());
                }
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mServiceBound) {
                    getContext().unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent i = new Intent(getContext(), BoundService.class);
                getContext().stopService(i);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), BoundService.class);
        getContext().startService(intent);
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            getContext().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
}
