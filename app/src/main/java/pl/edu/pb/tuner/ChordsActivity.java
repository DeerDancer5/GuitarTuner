package pl.edu.pb.tuner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChordsActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);
        BottomNavigationView bottomNavigationView= findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chords);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.bottom_chords) {
                return true;
            }
            else if(item.getItemId()==R.id.bottom_tuner) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            else if(item.getItemId()==R.id.bottom_metronome) {
                startActivity(new Intent(getApplicationContext(),MetronomeActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }

            return false;
        });
    }
}