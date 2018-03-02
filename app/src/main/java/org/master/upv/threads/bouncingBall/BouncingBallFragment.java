package org.master.upv.threads.bouncingBall;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.master.upv.threads.R;

import java.util.zip.Inflater;

public class BouncingBallFragment extends Fragment {
    public BouncingBallFragment() {
        // Required empty public constructor
    }

    public static BouncingBallFragment newInstance() {
        return new BouncingBallFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new BouncingBallView(getContext());
    }
}
