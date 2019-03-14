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

public class SurpriseActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Integer> sequence;
    private Handler handler;

    private ImageButton rooster_imageButton00;
    private ImageButton rooster_imageButton01;
    private ImageButton rooster_imageButton10;
    private ImageButton rooster_imageButton11;

    private TextView textview_player;
    private boolean player1;

    private int rooster00;
  /*  private int rooster01;
    private int rooster10;
    private int rooster11;*/
    private int selection;

    private SoundPool soundPool;

    private Set<Integer> soundsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise);

        soundsLoaded = new HashSet<Integer>();

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

        rooster00 = soundPool.load(this, R.raw.rooster, 1);
       /* rooster01 = soundPool.load(this, R.raw.rooster, 1);
        rooster10 = soundPool.load(this, R.raw.rooster, 1);
        rooster11 = soundPool.load(this, R.raw.rooster, 1);
*/
        textview_player = findViewById(R.id.textview_player);

        rooster_imageButton00 = findViewById(R.id.rooster_imageButton00);
        rooster_imageButton00.setOnClickListener(this);
        rooster_imageButton01 = findViewById(R.id.rooster_imageButton01);
        rooster_imageButton01.setOnClickListener(this);
        rooster_imageButton10 = findViewById(R.id.rooster_imageButton10);
        rooster_imageButton10.setOnClickListener(this);
        rooster_imageButton11 = findViewById(R.id.rooster_imageButton11);
        rooster_imageButton11.setOnClickListener(this);


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Rules!");
        dialog.setMessage("In this game every lens has the same sound and color," +
                "(red, green, blue, yellow). With no sound or color cues to rely on," +
                "can you remember and repeat the signals by location only? ");
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
        startGame();

        if (player1 = true) {
            if (rooster_imageButton00.isSelected()){
                buttonColors(rooster_imageButton00, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(0);
            }
            if (rooster_imageButton01.isSelected()){
                buttonColors(rooster_imageButton01, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(0);
            }
            if (rooster_imageButton10.isSelected()){
                buttonColors(rooster_imageButton10, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(0);
            }
            if (rooster_imageButton11.isSelected()){
                buttonColors(rooster_imageButton11, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(0);
            }
        }

    }

    /*The computer goes first and then it is the players turn, use a handler to
    to delay when the game starts */
    private void startGame() {

        textview_player.setText("SIMON TURN");
        player1=false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sequence();
            }
        }, 3000);
    }

    private void gameOver() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("GAME OVER ");
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    //Turning the button colors from light to dark
    private void buttonColors(final ImageButton button, final int dark, int light) {
        button.setBackgroundColor(light);

        if (rooster_imageButton00.isSelected() || rooster_imageButton11.isSelected() ||
        rooster_imageButton10.isSelected() || rooster_imageButton01.isSelected()) {
            playSound(rooster00);
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
        if (x >= 0 && x <= 3) {
            playSound(rooster00);
            buttonColors(rooster_imageButton00, getColor(R.color.blue), getColor(
                    R.color.light_blue));
            buttonColors(rooster_imageButton01, getColor(R.color.blue), getColor(
                    R.color.light_blue));
            buttonColors(rooster_imageButton10, getColor(R.color.blue), getColor(
                    R.color.light_blue));
            buttonColors(rooster_imageButton11, getColor(R.color.blue), getColor(
                    R.color.light_blue));
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
        if (x<sequence.size()){

            activateButton(sequence.get(x));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSequence( x +1);
                }
            }, 800);
        } else {
            textview_player.setText("PLAYER TURN");
            player1=true;
            selection=0;
        }

    }

    private void compareSequence (int x){
        if(x==sequence.get(selection)) {
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
