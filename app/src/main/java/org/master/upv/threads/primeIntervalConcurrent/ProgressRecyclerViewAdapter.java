package org.master.upv.threads.primeIntervalConcurrent;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.master.upv.threads.R;

import java.util.ArrayList;
import java.util.List;

public class ProgressRecyclerViewAdapter extends RecyclerView.Adapter<ProgressRecyclerViewAdapter.ViewHolder> {
    private final List<Progress> mValues;
    private final List<MyAsyncTask> tasks;
    private final PrimeIntervalConcurrentFragment.PrimeCheckListener listener;

    public ProgressRecyclerViewAdapter(PrimeIntervalConcurrentFragment.PrimeCheckListener listener) {
        this.mValues = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        long actual = mValues.get(position).getActual();
        holder.actualNumber.setText(String.valueOf(actual));
        holder.progressCheck.setProgress(mValues.get(position).getProgress());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addTasks(int number) {
        for (int i = 0; i < number; i++) {
            mValues.add(new Progress());
            final MyAsyncTask task = new MyAsyncTask(tasks.size(), new TaskListener() {
                @Override
                public void onPreExecute(int idx) {
                    mValues.get(idx).setProgress(0);
                    notifyItemChanged(idx);
                }

                @Override
                public void onProgressUpdate(int idx, double progreso) {
                    mValues.get(idx).setProgress((int) (progreso*100));
                    notifyItemChanged(idx);
                }

                @Override
                public void onPostExecute(int idx, boolean resultado) {
                    mValues.get(idx).setProgress(100);
                    listener.onProgressFinish(idx, mValues.get(idx).getActual(), resultado);
                    notifyItemChanged(idx);
                }

                @Override
                public void onCancelled(int idx) {
                    listener.onProgressFinish(idx, mValues.get(idx).getActual(), false);
                }
            });
            tasks.add(task);
        }
    }

    public void executeTasks(int index, long toCheck){
        mValues.get(index).setActual(toCheck);
        if(tasks.get(index).getStatus() != AsyncTask.Status.RUNNING){
            tasks.set(index, new MyAsyncTask(index, new TaskListener() {
                @Override
                public void onPreExecute(int idx) {
                    mValues.get(idx).setProgress(0);
                    notifyItemChanged(idx);
                }

                @Override
                public void onProgressUpdate(int idx, double progreso) {
                    mValues.get(idx).setProgress((int) (progreso*100));
                    notifyItemChanged(idx);
                }

                @Override
                public void onPostExecute(int idx, boolean resultado) {
                    mValues.get(idx).setProgress(100);
                    listener.onProgressFinish(idx, mValues.get(idx).getActual(), resultado);
                    notifyItemChanged(idx);
                }

                @Override
                public void onCancelled(int idx) {
                    listener.onProgressFinish(idx, mValues.get(idx).getActual(), false);
                }
            }));
        }
        tasks.get(index).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, toCheck);
        notifyItemChanged(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView actualNumber;
        public final ProgressBar progressCheck;
        public Progress mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            actualNumber = view.findViewById(R.id.actualNumber);
            progressCheck = view.findViewById(R.id.progressCheck);
        }
    }
}
