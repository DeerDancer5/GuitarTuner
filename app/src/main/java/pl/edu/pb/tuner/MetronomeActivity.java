package pl.edu.pb.tuner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MetronomeActivity extends AppCompatActivity {

    private Button startButton;

    private Metronome metronome;

    private TextView bpm;

    private SeekBar seekBar;
    private int tempo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        tempo = 100;

        bpm = findViewById(R.id.textBPM);
        startButton = findViewById(R.id.startButton);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMin(30);

        metronome = new Metronome(tempo, getApplicationContext());

        bpm.setText(tempo + " BPM");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(metronome.isRunning()) {
                    metronome.stop();
                    startButton.setText("Start");
                }
                else {
                    metronome.start();
                    startButton.setText("Stop");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               metronome.setTempo(progress);
               bpm.setText(progress + " BPM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BottomNavigationView bottomNavigationView= findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_metronome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            metronome.stop();
            if(item.getItemId()==R.id.bottom_metronome) {
                return true;
            }
            else if(item.getItemId()==R.id.bottom_tuner) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            else if(item.getItemId()==R.id.bottom_chords) {
                startActivity(new Intent(getApplicationContext(),ChordsActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
        }

            return false;
        });
    }
}