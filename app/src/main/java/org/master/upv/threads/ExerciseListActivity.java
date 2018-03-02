package org.master.upv.threads;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.master.upv.threads.bouncingBall.BouncingBallFragment;
import org.master.upv.threads.calculator.CalculatorSHA1Fragment;
import org.master.upv.threads.chronometer.ChronometerFragment;
import org.master.upv.threads.headless.HeadlessViewFragment;
import org.master.upv.threads.messenger.ChronometerMessengerFragment;
import org.master.upv.threads.prime.PrimeAsyncTaskFragment;
import org.master.upv.threads.primeInterface.PrimeInterfaceAsyncTaskFragment;
import org.master.upv.threads.primeInterval.PrimeIntervalFragment;
import org.master.upv.threads.primeIntervalConcurrent.PrimeIntervalConcurrentFragment;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.exercise_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.exercise_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<String> exercises = new ArrayList<>();

        exercises.add("Checkear si un número es primo mediante AsyncTask");
        exercises.add("Checkear si un número es primo mediante AsyncTask en Clase Independiente");
        exercises.add("Checkear si un número es primo mediante AsyncTask en Fragment Oculto");
        exercises.add("Checkear numeros primos en invervalo sequencial");
        exercises.add("Checkear numeros primos en invervalo concurrente");
        exercises.add("Descarga de imágenes y carga dinámica en UI");
        exercises.add("Cronometro basado en servicios");
        exercises.add("Cronometro basado en servicios y mensajero");
        exercises.add("Calculadora SHA1");
        exercises.add("Calculadora SHA1 con Receptor");
        exercises.add("Checkear si un numero es primo mediante Servicios");
        exercises.add("Checkear si un numero es primo mediante Servicios con uso de Intenciones");
        exercises.add("Checkear si un numero es primo mediante Servicios con uso de Notificaciones");
        exercises.add("Pelota que rebota");

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, exercises, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final ExerciseListActivity mParentActivity;
        private final List<String> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                if (mTwoPane) {
                    Fragment fragment;
                    switch (position){
                        case 0:
                            //Checkear si un número es primo mediante AsyncTask
                            fragment = PrimeAsyncTaskFragment.newInstance();
                            break;
                        case 1:
                            //Checkear si un número es primo mediante AsyncTask en Clase Independiente
                            fragment = PrimeInterfaceAsyncTaskFragment.newInstance();
                            break;
                        case 2:
                            //Checkear si un número es primo mediante AsyncTask en Fragment Oculto
                            fragment = HeadlessViewFragment.newInstance();
                            break;
                        case 3:
                            //Checkear numeros primos en invervalo sequencial
                            fragment = PrimeIntervalFragment.newInstance();
                            break;
                        case 4:
                            //Checkear numeros primos en invervalo concurrente
                            fragment = PrimeIntervalConcurrentFragment.newInstance();
                            break;
                        case 5:
                            //Descarga de imágenes y carga dinámica en UI
                            //TODO
                            fragment = new ExerciseDetailFragment();
                            break;
                        case 6:
                            //Cronometro basado en servicios
                            fragment = ChronometerFragment.newInstance();
                            break;
                        case 7:
                            //Cronometro basado en servicios y mensajero
                            fragment = ChronometerMessengerFragment.newInstance();
                            break;
                        case 8:
                            //Calculadora SHA1
                            fragment = CalculatorSHA1Fragment.newInstance();
                            break;
                        case 9:
                            //Calculadora SHA1 con Receptor
                            //TODO
                            fragment = new ExerciseDetailFragment();
                            break;
                        case 10:
                            //Checkear si un numero es primo mediante Servicios
                            //TODO
                            fragment = new ExerciseDetailFragment();
                            break;
                        case 11:
                            //Checkear si un numero es primo mediante Servicios con uso de Intenciones
                            //TODO
                            fragment = new ExerciseDetailFragment();
                            break;
                        case 12:
                            //Checkear si un numero es primo mediante Servicios con uso de Notificaciones
                            //TODO
                            fragment = new ExerciseDetailFragment();
                            break;
                        case 13:
                            //Pelota que rebota
                            fragment = BouncingBallFragment.newInstance();
                            break;
                        default:
                            fragment = new ExerciseDetailFragment();
                            break;
                    }

                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.exercise_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ExerciseDetailActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ExerciseListActivity parent,
                                      List<String> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.exercise_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(position));
            holder.mContentView.setText(mValues.get(position));

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
