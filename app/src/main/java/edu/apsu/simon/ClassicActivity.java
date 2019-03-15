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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ClassicActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Integer> sequence;
    private Handler handler;

    private ImageButton rain_imageButton;
    private ImageButton snow_imageButton;
    private ImageButton tornado_imageButton;
    private ImageButton thunderstorm_imageButton;

    private TextView textview_player;
    private boolean player;

    private int rainId;
    private int snowId;
    private int thunderstormId;
    private int tornadoId;
    private int looserId;
    private int selection;

    private SoundPool soundPool;

    private Set<Integer> soundsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);

        sequence = new ArrayList<>();

        handler = new Handler();

        soundsLoaded = new HashSet<Integer>();
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

        rainId = soundPool.load(this, R.raw.rain, 1);
        snowId = soundPool.load(this, R.raw.snow, 1);
        thunderstormId = soundPool.load(this, R.raw.thunder, 1);
        tornadoId = soundPool.load(this, R.raw.tornado, 1);
        looserId = soundPool.load(this, R.raw.looser, 1);

        textview_player = findViewById(R.id.textview_player);

        rain_imageButton =  findViewById(R.id.rain_imageButton);
        snow_imageButton = findViewById(R.id.snow_imageButton);
        thunderstorm_imageButton = findViewById(R.id.thunderstorm_imageButton);
        tornado_imageButton = findViewById(R.id.tornado_imageButton);

        rain_imageButton.setOnClickListener(this);
        snow_imageButton.setOnClickListener(this);
        thunderstorm_imageButton.setOnClickListener(this);
        tornado_imageButton.setOnClickListener(this);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Rules!");
        dialog.setMessage("Simon goes first by lighting up one lenses giving you the " +
                "first signal. Repeat this signal by pressing that lens. As the game " +
                "goes on Simon will add on more buttons to the seqence. If you press the incorrect " +
                "sequence you loose.");
        dialog.setPositiveButton("Let's Play",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startGame();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {

        if (player) {
            if (v.getId() == rain_imageButton.getId()){
                buttonColors(rain_imageButton, getColor(R.color.yellow), getColor(
                        R.color.light_yellow));
                compareSequence(0);
            } else if (v.getId() == snow_imageButton.getId()) {
                buttonColors(snow_imageButton, getColor(R.color.green),
                        R.color.light_green);
                compareSequence(1);
            } else if (v.getId() == thunderstorm_imageButton.getId()) {
                buttonColors(thunderstorm_imageButton, getColor(R.color.blue),
                        R.color.light_blue);
                compareSequence(2);
            } else if (v.getId() == tornado_imageButton.getId()) {
                buttonColors(tornado_imageButton, getColor(R.color.red),
                        R.color.red);
                compareSequence(3);
            }
        }

    }

    /*The computer goes first and then it is the players turn, use a handler to
   to delay when the game starts */
    private void startGame() {

        textview_player.setText("SIMON TURN");
        player=false;
        rain_imageButton.setEnabled(false);
        snow_imageButton.setEnabled(false);
        thunderstorm_imageButton.setEnabled(false);
        tornado_imageButton.setEnabled(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sequence();
            }
        }, 3000);
    }

    private void gameOver() {
        playSound(looserId);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("GAME OVER, YOU LOST!");
        dialog.setMessage("Press play to play again or hit the back button to end game");
        dialog.setPositiveButton("Play",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startGame();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    //Turning the button colors from light to dark
    private void buttonColors(final ImageButton button, final int dark, int light) {
        button.setBackgroundColor(light);

        if (rain_imageButton.isSelected()) {
            playSound(rainId);
        }
        if (snow_imageButton.isSelected()) {
            playSound(snowId);
        }
        if (thunderstorm_imageButton.isSelected()) {
            playSound(thunderstormId);
        }
        if (snow_imageButton.isSelected()) {
            playSound(tornadoId);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundColor(dark);
            }
        }, 500);
    }

    private void playSound(int soundId) {
        if (soundsLoaded.contains(soundId)) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    //Button is activated based on the choice from the computer
    private void activateButton(int x) {
        if (x == 0) {
            playSound(rainId);
            buttonColors(rain_imageButton, getColor(R.color.yellow), getColor(
                    R.color.light_yellow));
        } else if (x == 1) {
            playSound(snowId);
            buttonColors(snow_imageButton, getColor(R.color.green),
                    R.color.light_green);
        } else if (x == 2) {
            playSound(thunderstormId);
            buttonColors(thunderstorm_imageButton, getColor(R.color.blue),
                    R.color.light_blue);
        } else if (x == 3) {
            playSound(tornadoId);
            buttonColors(tornado_imageButton, getColor(R.color.red),
                    R.color.red);
        }
    }

    //Changes the sequence to what buttons are being pressed
    private void sequence() {
        Random random = new Random();
        int key = random.nextInt(4);
        sequence.add(key);
        displaySequence(0);
    }

    //Displays sequence for the player
    private void displaySequence (final int x){
        if (x<sequence.size()){

            activateButton(sequence.get(x));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displaySequence( x + 1);
                }
            }, 3000);
        } else {
            rain_imageButton.setEnabled(true);
            snow_imageButton.setEnabled(true);
            thunderstorm_imageButton.setEnabled(true);
            tornado_imageButton.setEnabled(true);
            textview_player.setText("PLAYER TURN");
            player=true;
            selection=0;
        }

    }

    private void compareSequence (int x){
        if(x==sequence.get(selection)) {
            if (x == 0 && rain_imageButton.isSelected()){
                Log.i("CORRECT", "i");
            } else if (x == 1 && snow_imageButton.isSelected()) {
                Log.i("CORRECT", "i");
            } else if (x == 2 && thunderstorm_imageButton.isSelected()) {
                Log.i("CORRECT", "i");
            } else if (x == 3 && tornado_imageButton.isSelected()) {
                Log.i("CORRECT", "i");
            }
            selection++;
        } else {
            gameOver();
        }

        if (selection>=sequence.size()){
            startGame();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
