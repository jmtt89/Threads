package org.master.upv.threads.primeInterface;

interface TaskListener {
    void onPreExecute();
    void onProgressUpdate(double progreso);
    void onPostExecute(boolean resultado);
    void onCancelled();
}
