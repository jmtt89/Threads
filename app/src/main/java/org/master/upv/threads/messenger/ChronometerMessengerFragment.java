package org.master.upv.threads.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.master.upv.threads.R;

import java.lang.ref.WeakReference;

public class ChronometerMessengerFragment extends Fragment {
    private Messenger mBoundServiceMessenger;
    private boolean mServiceConnected = false;
    private TextView mTimestampText;
    private final Messenger mActivityMessenger = new Messenger(new FragmentHandler(this));

    public ChronometerMessengerFragment() {
        // Required empty public constructor
    }

    public static ChronometerMessengerFragment newInstance() {
        return new ChronometerMessengerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chronometer, container, false);

        mTimestampText = root.findViewById(R.id.timestamptext);
        Button printTimestampButton = root.findViewById(R.id.btnPrintTimeStamp);
        Button stopServiceButon = root.findViewById(R.id.btnStopService);
        printTimestampButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mServiceConnected) {
                    try {
                        Message msg = Message.obtain(null, MessengerService.MSG_GET_TIMESTAMP, 0, 0);
                        msg.replyTo = mActivityMessenger;
                        mBoundServiceMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        stopServiceButon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mServiceConnected) {
                    getContext().unbindService(mServiceConnection);
                    mServiceConnected = false;
                }
                Intent intent = new Intent(getContext(), MessengerService.class);
                getContext().stopService(intent);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), MessengerService.class);
        getContext().startService(intent);
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceConnected) {
            getContext().unbindService(mServiceConnection);
            mServiceConnected = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override public void onServiceDisconnected(ComponentName name) {
            mBoundServiceMessenger = null;
            mServiceConnected = false;
        }

        @Override public void onServiceConnected(ComponentName name,
                                                 IBinder service) {
            mBoundServiceMessenger = new Messenger(service);
            mServiceConnected = true;
        }
    };

    static class FragmentHandler extends Handler {
        private final WeakReference<ChronometerMessengerFragment> mFragment;

        public FragmentHandler(ChronometerMessengerFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_GET_TIMESTAMP: {
                    mFragment.get().mTimestampText.setText(msg.getData().getString("timestamp"));
                }
            }
        }
    }
}
