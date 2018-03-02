package org.master.upv.threads.primeIntervalConcurrent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import org.master.upv.threads.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class PrimeIntervalConcurrentFragment extends Fragment{
    private static final String TAG = PrimeIntervalConcurrentFragment.class.getName();
    private EditText inputStartIntervalField, inputEndIntervalField;
    private TextView primeList;
    private Button primeCheckButton;
    private RecyclerView progressList;

    private ProgressRecyclerViewAdapter adapter;
    private List<Long> toCheck = new ArrayList<>();
    private int cores;

    public PrimeIntervalConcurrentFragment() {
        // Required empty public constructor
    }

    public static PrimeIntervalConcurrentFragment newInstance() {
        return new PrimeIntervalConcurrentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_prime_interval_concurrent_check, container, false);

        cores = getNumOfCores();

        inputStartIntervalField = root.findViewById(R.id.inputStartIntervalField);
        inputEndIntervalField = root.findViewById(R.id.inputEndIntervalField);

        progressList = root.findViewById(R.id.progressList);

        // Set the adapter
        progressList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProgressRecyclerViewAdapter(new PrimeCheckListener() {
            @Override
            public void onProgressFinish(int index, long number, boolean isPrime) {
                if(!toCheck.isEmpty()){
                    if(isPrime){
                        String tmp = primeList.getText().toString();
                        primeList.setText(tmp+",FIN");
                    }
                    adapter.executeTasks(index, toCheck.remove(0));
                }else{
                    String tmp = primeList.getText().toString();
                    primeList.setText(tmp+",FIN");
                }

            }
        });
        progressList.setAdapter(adapter);

        primeList = root.findViewById(R.id.primeList);
        primeCheckButton = root.findViewById(R.id.primeCheckButton);

        primeCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerPrimeCheck(view);
            }
        });

         return root;
    }

    public void triggerPrimeCheck(View view) {
        long start = Long.parseLong(inputStartIntervalField.getText().toString());
        long end = Long.parseLong(inputEndIntervalField.getText().toString());
        adapter.addTasks(cores);
        for (int i = 0; i < end - start; i++) {
            if(i < cores){
                adapter.executeTasks(i, start+i);
            }else{
                toCheck.add(start+i);
            }
        }
    }

    private int getNumOfCores() {
        try {
            return new File("/sys/devices/system/cpu/")
                    .listFiles(new FileFilter(){
                        public boolean accept(File params){
                            return Pattern.matches("cpu[0-9]", params.getName());
                        }
                    }).length;
        } catch (Exception e){
            Log.e(TAG, "Error determinando el número de procesadores");
        }
        return 1;
    }

    interface PrimeCheckListener{
        void onProgressFinish(int index, long number, boolean isPrime);
    }
}
