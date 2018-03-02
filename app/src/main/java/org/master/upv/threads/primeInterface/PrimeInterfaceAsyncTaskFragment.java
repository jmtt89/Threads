package org.master.upv.threads.primeInterface;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.master.upv.threads.R;
import org.master.upv.threads.commons.Utils;
import org.master.upv.threads.prime.PrimeAsyncTaskFragment;

public class PrimeInterfaceAsyncTaskFragment extends Fragment implements TaskListener{
    private static final String TAG = PrimeInterfaceAsyncTaskFragment.class.getName();
    private EditText inputField, resultField;
    private ProgressBar progressCheck;
    private Button primeCheckButton;
    private MyAsyncTask mAsyncTask;

    public PrimeInterfaceAsyncTaskFragment() {
        // Required empty public constructor
    }

    public static PrimeInterfaceAsyncTaskFragment newInstance() {
        return new PrimeInterfaceAsyncTaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_prime_check, container, false);

        inputField = root.findViewById(R.id.inputField);
        resultField = root.findViewById(R.id.resultField);
        progressCheck = root.findViewById(R.id.progressCheck);
        progressCheck.setMax(100);
        primeCheckButton = root.findViewById(R.id.primecheckbutton);

        primeCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerPrimeCheck(view);
            }
        });

        return root;
    }

    @Override
    public void onStop() {
        if(mAsyncTask!=null && mAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            mAsyncTask.cancel(true);
        }
        super.onStop();
    }

    public void triggerPrimeCheck(View view) {
        if(mAsyncTask == null || mAsyncTask.getStatus() != AsyncTask.Status.RUNNING){
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": triggerPrimeCheck() comienza");
            long parameter = Long.parseLong(inputField.getText().toString());
            mAsyncTask = new MyAsyncTask(this);
            mAsyncTask.execute(parameter);
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": triggerPrimeCheck() termina");
        } else {
            Log.v(TAG, "Cancelando test " + Thread.currentThread().getId());
            mAsyncTask.cancel(true);
        }
    }

    @Override public void onPreExecute() {
        Utils.lockScreenOrientation(getActivity());
        resultField.setText("");
        primeCheckButton.setText("CANCELAR");
    }

    @Override public void onProgressUpdate(double progress) {
        progressCheck.setProgress((int) (progress*100));
        //resultField.setText(String.format("%.1f%% completado", progress * 100));

    }

    @Override public void onPostExecute(boolean resultado) {
        resultField.setText(resultado + "");
        primeCheckButton.setText("¿ES PRIMO?");
        Utils.unlockScreenOrientation(getActivity());
    }

    @Override public void onCancelled() {
        resultField.setText("Proceso cancelado");
        primeCheckButton.setText("¿ES PRIMO?");
        Utils.unlockScreenOrientation(getActivity());
    }
}
