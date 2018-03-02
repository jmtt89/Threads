package org.master.upv.threads.prime;

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


public class PrimeAsyncTaskFragment extends Fragment {
    private static final String TAG = PrimeAsyncTaskFragment.class.getName();
    private EditText inputField, resultField;
    private ProgressBar progressCheck;
    private Button primeCheckButton;
    private MyAsyncTask mAsyncTask;

    public PrimeAsyncTaskFragment() {
        // Required empty public constructor
    }

    public static PrimeAsyncTaskFragment newInstance() {
        return new PrimeAsyncTaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            mAsyncTask = new MyAsyncTask();
            mAsyncTask.execute(parameter);
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": triggerPrimeCheck() termina");
        } else {
            Log.v(TAG, "Cancelando test " + Thread.currentThread().getId());
            mAsyncTask.cancel(true);
        }
    }

    private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
        @Override protected void onPreExecute() {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onPreExecute()");
            Utils.lockScreenOrientation(getActivity());
            resultField.setText("");
            primeCheckButton.setText("CANCELAR");
        }

        @Override protected Boolean doInBackground(Long... n) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": Comienza doInBackground()");
            long numComprobar = n[0];
            if (numComprobar < 2 || numComprobar % 2 == 0)
                return false;
            double limite = Math.sqrt(numComprobar) + 0.0001;
            double progreso = 0;
            for (long factor = 3; factor < limite && !isCancelled(); factor += 2) {
                if (numComprobar % factor == 0)
                    return false;
                if (factor > limite * progreso / 100) {
                    publishProgress(progreso / 100);
                    progreso += 5;
                }
            }
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": Finaliza doInBackground()");
            Log.d(TAG, "doInBackground: Status: "+ getStatus());
            return true;
        }

        @Override protected void onProgressUpdate(Double... progress) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onProgressUpdate()");
            Log.d(TAG, "onProgressUpdate: Status: "+ getStatus());
            progressCheck.setProgress((int) (progress[0]*100));
            //resultField.setText(String.format("%.1f%% completed", progress[0] * 100));
        }

        @Override protected void onPostExecute(Boolean isPrime) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onPostExecute()");
            Log.d(TAG, "onPostExecute: Status: "+ getStatus());
            resultField.setText(isPrime + "");
            primeCheckButton.setText("¿ES PRIMO?");
            Utils.unlockScreenOrientation(getActivity());
        }

        @Override protected void onCancelled() {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onCancelled");
            Log.d(TAG, "onCancelled: Status: "+ getStatus());
            super.onCancelled();
            resultField.setText("Proceso cancelado");
            primeCheckButton.setText("¿ES PRIMO?");
            Utils.unlockScreenOrientation(getActivity());
        }
    }
}
