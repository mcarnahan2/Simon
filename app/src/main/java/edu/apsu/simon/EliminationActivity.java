package edu.apsu.simon;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EliminationActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Integer> sequence;

    private ImageButton rain_imageButton;
    private ImageButton snow_imageButton;
    private ImageButton tornado_imageButton;
    private ImageButton thunderstorm_imageButton;

    private TextView textview_player;
    private boolean player1;
    private boolean player2;
    private boolean player3;

    private SoundPool soundPool;

    private Set<Integer> soundsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimination);

        soundsLoaded = new HashSet<Integer>();
        initialView();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Rules!");
        dialog.setMessage("Player 1 goes first once Player 1 has lost pass the game" +
                "to the next player. Each time a player is eliminated, start a new game" +
                "players. Keep going until only one player" +
                "wins the game! ");
        dialog.setPositiveButton("Let's Play",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        startGame();

    }

    @Override
    public void onClick(View v) {
        if (player1) {
            if (rain_imageButton.isSelected()){
                buttonColors(rain_imageButton, getColor(R.color.yellow), getColor(
                        R.color.light_yellow));
            }
            if (snow_imageButton.isSelected()) {
                buttonColors(snow_imageButton, getColor(R.color.green),
                        R.color.light_green);
            }
            if (thunderstorm_imageButton.isSelected()) {
                buttonColors(thunderstorm_imageButton, getColor(R.color.blue),
                        R.color.light_blue);
            }
            if (tornado_imageButton.isSelected()) {
                buttonColors(tornado_imageButton, getColor(R.color.red),
                        R.color.red);
            }
        }

    }

    //What the game looks like when it initially starts
    private void initialView() {
        sequence = new ArrayList<>();

        textview_player = findViewById(R.id.textview_player);

        rain_imageButton =  findViewById(R.id.rain_imageButton);
        snow_imageButton = findViewById(R.id.snow_imageButton);
        thunderstorm_imageButton = findViewById(R.id.thunderstorm_imageButton);
        tornado_imageButton = findViewById(R.id.tornado_imageButton);

        rain_imageButton.setOnClickListener(this);
        snow_imageButton.setOnClickListener(this);
        thunderstorm_imageButton.setOnClickListener(this);
        tornado_imageButton.setOnClickListener(this);

    }

    /*The computer goes first and then it is the players turn, use a handler to
   to delay when the game starts */
    private void startGame() {
        player1=false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sequence();
            }
        }, 5000);
    }

    private void gameOver() {

    }

    //Turning the button colors from light to dark
    private void buttonColors(final ImageButton button, final int dark, int light) {
        button.setBackgroundColor(light);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundColor(dark);
            }
        }, 500);
    }

    //Button is activated based on the choice from the computer
    private void activateButton(int x) {
        if (x == 0) {
            buttonColors(rain_imageButton, getColor(R.color.yellow), getColor(
                    R.color.light_yellow));
        } else if (x == 1) {
            buttonColors(snow_imageButton, getColor(R.color.green),
                    R.color.light_green);
        } else if (x == 2) {
            buttonColors(thunderstorm_imageButton, getColor(R.color.blue),
                    R.color.light_blue);
        } else if (x == 3) {
            buttonColors(tornado_imageButton, getColor(R.color.red),
                    R.color.red);
        }
    }

    //Changes the sequence to what buttons are being pressed
    private void sequence() {
        Random random = new Random();
        int key = random.nextInt(4);
        sequence.add(key);
        showSequence(0);
    }

    //Displays sequence for the player
    private void showSequence (final int x){
        if (x<sequence.size()) {
            activateButton(sequence.get(x));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence( x +1);
                }
            }, 500);
        } else {
            player1=true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(1);
        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) { // success
                    soundsLoaded.add(sampleId);
                    Log.i("SOUND", "Sound loaded " + sampleId);
                } else {
                    Log.i("SOUND", "Error cannot load sound status = " + status);
                }
            }
        });

        final int rainId = soundPool.load(this, R.raw.rain, 1);
        findViewById(R.id.rain_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(rainId);
            }
        });

        final int snowId = soundPool.load(this, R.raw.snow, 1);
        findViewById(R.id.snow_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(snowId);
            }
        });

        final int thunderId = soundPool.load(this, R.raw.thunder, 1);
        findViewById(R.id.thunderstorm_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(thunderId);
            }
        });

        final int tornadoId = soundPool.load(this, R.raw.tornado, 1);
        findViewById(R.id.tornado_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(tornadoId);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
    }

    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

}
