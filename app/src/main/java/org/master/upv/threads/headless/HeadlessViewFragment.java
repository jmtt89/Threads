package org.master.upv.threads.headless;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.master.upv.threads.R;
import org.master.upv.threads.commons.Utils;

public class HeadlessViewFragment extends Fragment implements HeadlessFragment.TaskListener {
    private static final String TAG = HeadlessViewFragment.class.getName();
    private EditText inputField, resultField;
    private ProgressBar progressCheck;
    private Button primeCheckButton;

    public HeadlessViewFragment() {
        // Required empty public constructor
    }

    public static HeadlessViewFragment newInstance() {
        return new HeadlessViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HeadlessFragment fragment = (HeadlessFragment)getFragmentManager().findFragmentByTag(HeadlessFragment.TAG);
        if(fragment != null){
            fragment.attachTaskListener(this);
        }
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void triggerPrimeCheck(View view) {
        HeadlessFragment fragment = (HeadlessFragment)getFragmentManager().findFragmentByTag(HeadlessFragment.TAG);

        if(fragment == null){
            long parameter = Long.parseLong(inputField.getText().toString());
            Bundle parametros = new Bundle();
            parametros.putLong("numComprobar", parameter);
            fragment = HeadlessFragment.newInstance(parametros);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(fragment, HeadlessFragment.TAG);
            ft.commit();
            fragment.attachTaskListener(this);
        }else{
            fragment.stopPrimeCheck();
        }
    }

    @Override
    public void onPreExecute() {
        resultField.setText("");
        primeCheckButton.setText("CANCELAR");
    }

    @Override
    public void onProgressUpdate(double progress) {
        progressCheck.setProgress((int) (progress*100));
        //resultField.setText(String.format("%.1f%% completado", progress * 100));
    }

    @Override
    public void onPostExecute(boolean resultado) {
        resultField.setText(resultado + "");
        primeCheckButton.setText("¿ES PRIMO?");
        HeadlessFragment fragment = (HeadlessFragment)getFragmentManager().findFragmentByTag(HeadlessFragment.TAG);
        if(fragment != null){
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onCancelled() {
        resultField.setText("Proceso cancelado");
        primeCheckButton.setText("¿ES PRIMO?");
        HeadlessFragment fragment = (HeadlessFragment)getFragmentManager().findFragmentByTag(HeadlessFragment.TAG);
        if(fragment != null){
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
