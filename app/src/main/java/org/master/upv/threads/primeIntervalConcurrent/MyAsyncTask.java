package org.master.upv.threads.primeIntervalConcurrent;

import android.os.AsyncTask;
import android.util.Log;


class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
    private final static String TAG = MyAsyncTask.class.getName();
    private final int index;
    private final TaskListener listener;

    public MyAsyncTask(int index, TaskListener listener) {
        this.index = index;
        this.listener = listener;
    }

    @Override protected void onPreExecute() {
        Log.v(TAG,"Thread "+Thread.currentThread().getId()+": onPreExecute()");
        listener.onPreExecute(index);
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
        Log.v(TAG,"Thread "+Thread.currentThread().getId() + ": onProgressUpdate()");
        listener.onProgressUpdate(index, progress[0]);
    }

    @Override protected void onPostExecute(Boolean isPrime) {
        Log.v(TAG,"Thread"+Thread.currentThread().getId()+": onPostExecute()");
        listener.onPostExecute(index, isPrime);
    }

    @Override protected void onCancelled() {
        Log.v(TAG,"Thread "+Thread.currentThread().getId() + ": onCancelled");
        listener.onCancelled(index);
        super.onCancelled();
    }
}