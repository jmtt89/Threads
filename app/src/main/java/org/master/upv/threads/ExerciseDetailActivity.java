package org.master.upv.threads;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.master.upv.threads.bouncingBall.BouncingBallFragment;
import org.master.upv.threads.calculator.CalculatorSHA1Fragment;
import org.master.upv.threads.chronometer.ChronometerFragment;
import org.master.upv.threads.headless.HeadlessViewFragment;
import org.master.upv.threads.messenger.ChronometerMessengerFragment;
import org.master.upv.threads.prime.PrimeAsyncTaskFragment;
import org.master.upv.threads.primeInterface.PrimeInterfaceAsyncTaskFragment;
import org.master.upv.threads.primeInterval.PrimeIntervalFragment;
import org.master.upv.threads.primeIntervalConcurrent.PrimeIntervalConcurrentFragment;


public class ExerciseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int position = getIntent().getIntExtra("position", -1);

        if (savedInstanceState == null) {
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

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.exercise_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ExerciseListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
