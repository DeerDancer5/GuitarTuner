package pl.edu.pb.tuner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ChordsActivity extends AppCompatActivity {

    private WebView webView;

    private ChordViewModel chordViewModel;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        BottomNavigationView bottomNavigationView= findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chords);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ChordAdapter adapter = new ChordAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chordViewModel = new ViewModelProvider(this).get(ChordViewModel.class);
        chordViewModel.findAll().observe(this,adapter::setChords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new ChordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chord chord) {
                showChordAlert(chord);
            }
        });





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

    public void showChordAlert(Chord chord) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_layout, null);
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(850);

        String name = chord.getLetter()+"_"+chord.getExtension();
        webView.loadUrl("https://api.uberchord.com/v1/embed/chords/" + name);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();

    }


    private class ChordHolder extends RecyclerView.ViewHolder {
        private TextView chordLetterTextView;

        public ChordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.chord_list_item,parent,false));

            chordLetterTextView = itemView.findViewById(R.id.chord_letter);
        }

        public void bind (Chord chord) {
            chordLetterTextView.setText(chord.getLetter()+chord.getExtension());
        }

    }

    private class ChordAdapter extends RecyclerView.Adapter<ChordHolder> {
        private List<Chord> chords;
        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            void onItemClick(Chord chord);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = (OnItemClickListener) listener;
        }

        @NonNull
        @Override
        public ChordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ChordHolder(getLayoutInflater(),parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ChordHolder holder, int position) {

            Chord currentChord = chords.get(position);
            holder.bind(currentChord);

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(currentChord);
                }
            });


        }

        @Override
        public int getItemCount() {
            if(chords != null) {
                return chords.size();
            }
            else {
                return 0;
            }
        }

        void setChords(List <Chord> chords) {
            this.chords = chords;
            notifyDataSetChanged();
        }
    }
}