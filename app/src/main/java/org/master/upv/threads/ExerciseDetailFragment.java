package org.master.upv.threads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseDetailFragment extends Fragment {

    public ExerciseDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.exercise_detail, container, false);

            ((TextView) rootView.findViewById(R.id.exercise_detail)).setText("TODO");

        return rootView;
    }
}
