package org.master.upv.threads.headless;

import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.master.upv.threads.commons.Utils;


public class HeadlessFragment extends Fragment {
    public static final String TAG = HeadlessFragment.class.getName();
    @Nullable
    private TaskListener taskListener;
    private MyAsyncTask myAsyncTask;
    private long numComprobar;

    public HeadlessFragment() {
    }

    public static HeadlessFragment newInstance(Bundle argumentos){
        HeadlessFragment f = new HeadlessFragment();
        if(argumentos != null){
            f.setArguments(argumentos);
        }
        return f;
    }

    public void attachTaskListener(TaskListener listener){
        this.taskListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle parameters = this.getArguments();
        if(parameters != null){
            this.numComprobar = parameters.getLong("numComprobar", 0);
        }
        startPrimeCheck();
    }

    private void startPrimeCheck(){
        if(myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) return;
        if(myAsyncTask == null || myAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute(this.numComprobar);
        }
    }

    public void stopPrimeCheck(){
        if(myAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTask.cancel(true);
        }
    }

    @Override public void onDetach() {
        this.taskListener = null;
        super.onDetach();
    }

    private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
        @Override protected void onPreExecute() {
            Log.v(TAG,"Thread "+Thread.currentThread().getId()+": onPreExecute()");
            if (taskListener != null) {
                taskListener.onPreExecute();
            }
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
            if (taskListener != null) {
                taskListener.onProgressUpdate(progress[0]);
            }
        }

        @Override protected void onPostExecute(Boolean isPrime) {
            Log.v(TAG,"Thread"+Thread.currentThread().getId()+": onPostExecute()");
            if (taskListener != null) {
                taskListener.onPostExecute(isPrime);
            }
        }

        @Override protected void onCancelled() {
            Log.v(TAG,"Thread "+Thread.currentThread().getId() + ": onCancelled");
            if (taskListener != null) {
                taskListener.onCancelled();
            }
            super.onCancelled();
        }
    }

    interface TaskListener {
        void onPreExecute();
        void onProgressUpdate(double progreso);
        void onPostExecute(boolean resultado);
        void onCancelled();
    }
}
