package org.master.upv.threads.primeIntervalConcurrent;

interface TaskListener {
    void onPreExecute(int idx);
    void onProgressUpdate(int idx, double progreso);
    void onPostExecute(int idx, boolean resultado);
    void onCancelled(int idx);
}
