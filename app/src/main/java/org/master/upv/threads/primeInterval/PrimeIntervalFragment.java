package org.master.upv.threads.primeInterval;

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
import android.widget.TextView;

import org.master.upv.threads.R;
import org.master.upv.threads.commons.Utils;


public class PrimeIntervalFragment extends Fragment {
    private static final String TAG = PrimeIntervalFragment.class.getName();
    private EditText inputStartIntervalField, inputEndIntervalField;
    private TextView actualNumber, primeList;
    private ProgressBar progressCheck;
    private Button primeCheckButton;
    private MyAsyncTask mAsyncTask;

    public PrimeIntervalFragment() {
        // Required empty public constructor
    }

    public static PrimeIntervalFragment newInstance() {
        return new PrimeIntervalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_prime_interval_check, container, false);

        inputStartIntervalField = root.findViewById(R.id.inputStartIntervalField);
        inputEndIntervalField = root.findViewById(R.id.inputEndIntervalField);
        actualNumber = root.findViewById(R.id.actualNumber);
        primeList = root.findViewById(R.id.primeList);
        progressCheck = root.findViewById(R.id.progressCheck);
        progressCheck.setMax(100);
        primeCheckButton = root.findViewById(R.id.primeCheckButton);

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
        if(mAsyncTask != null && mAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            mAsyncTask.cancel(true);
        }
        super.onStop();
    }

    public void triggerPrimeCheck(View view) {
        if(mAsyncTask == null || mAsyncTask.getStatus() != AsyncTask.Status.RUNNING){
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": triggerPrimeCheck() comienza");

            String startSt = inputStartIntervalField.getText().toString();
            String endSt = inputEndIntervalField.getText().toString();

            if(startSt.isEmpty()){
                startSt = "1";
            }

            if(endSt.isEmpty()){
                endSt = "1";
            }

            long start = Long.parseLong(startSt);
            long end = Long.parseLong(endSt);
            mAsyncTask = new MyAsyncTask();
            mAsyncTask.execute(start, end);
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": triggerPrimeCheck() termina");
        } else {
            Log.v(TAG, "Cancelando test " + Thread.currentThread().getId());
            mAsyncTask.cancel(true);
        }
    }

    private class MyAsyncTask extends AsyncTask<Long, Object, Boolean> {
        @Override protected void onPreExecute() {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onPreExecute()");
            Utils.lockScreenOrientation(getActivity());
            primeList.setText("");
            primeCheckButton.setText("CANCELAR");
        }

        @Override protected Boolean doInBackground(Long... n) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": Comienza doInBackground()");
            long start = n[0];
            long end = n[1];

            for (long i = start; i < end; i++) {
                //actualNumber.setText(String.valueOf(i));
                isPrime(i);
            }

            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": Finaliza doInBackground()");
            Log.d(TAG, "doInBackground: Status: "+ getStatus());
            return true;
        }

        private boolean isPrime(long numComprobar){
            if (numComprobar < 2 || numComprobar % 2 == 0)
                return false;
            double limite = Math.sqrt(numComprobar) + 0.0001;
            double progreso = 0;
            for (long factor = 3; factor < limite && !isCancelled(); factor += 2) {
                if (numComprobar % factor == 0)
                    return false;
                if (factor > limite * progreso / 100) {
                    publishProgress((int)(progreso / 100));
                    progreso += 5;
                }
            }
            publishProgress(-1, numComprobar);
            return true;
        }

        @Override protected void onProgressUpdate(Object... progress) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onProgressUpdate()");
            Log.d(TAG, "onProgressUpdate: Status: "+ getStatus());
            int prog = (int) progress[0];
            progressCheck.setProgress(prog*100);
            if(progress.length > 1){
                long resp = (long) progress[1];
                String tmp = primeList.getText().toString();
                primeList.setText(tmp+","+resp);

            }
            //resultField.setText(String.format("%.1f%% completed", progress[0] * 100));
        }

        @Override protected void onPostExecute(Boolean isPrime) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onPostExecute()");
            Log.d(TAG, "onPostExecute: Status: "+ getStatus());
            String tmp = primeList.getText().toString();
            primeList.setText(tmp+",FIN");

            primeCheckButton.setText("¿ES PRIMO?");
            Utils.unlockScreenOrientation(getActivity());
        }

        @Override protected void onCancelled() {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() + ": onCancelled");
            Log.d(TAG, "onCancelled: Status: "+ getStatus());
            super.onCancelled();
            String tmp = primeList.getText().toString();
            primeList.setText(tmp+",CANCELADO");
            primeCheckButton.setText("¿ES PRIMO?");
            Utils.unlockScreenOrientation(getActivity());
        }
    }
}
