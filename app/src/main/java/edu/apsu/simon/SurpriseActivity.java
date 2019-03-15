package edu.apsu.simon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

    private TextView hs;
    private TextView cs;

    private TextView textview_player;
    private boolean player1;

    private int rooster00;
    private int rooster01;
    private int rooster10;
    private int rooster11;
    private int looserId;
    private int selection;

    private SoundPool soundPool;

    private Set<Integer> soundsLoaded;

    private Context context;

    private int highScore;
    private int currentScore;

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
        rooster01 = soundPool.load(this, R.raw.rooster, 1);
        rooster10 = soundPool.load(this, R.raw.rooster, 1);
        rooster11 = soundPool.load(this, R.raw.rooster, 1);
        looserId = soundPool.load(this, R.raw.looser, 1);

        context = getApplicationContext();
        SharedPreferences prefs = this.getSharedPreferences("GET_HIGH_SCORE_SURPRISE", getApplicationContext().MODE_PRIVATE);
        highScore = prefs.getInt("HIGH_SCORE_SURPRISE", 0);

        SharedPreferences hScore= getSharedPreferences("GET_HIGH_SCORE_SURPRISE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = hScore.edit();
        editor.putInt("HIGH_SCORE_SURPRISE", highScore);
        editor.commit();

        hs = findViewById(R.id.high_score_textView);

        String hsString = Integer.toString(highScore);

        hs.setText(hsString);
        cs = findViewById(R.id.current_score_textView);

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

        if (player1) {
            if (v.getId() == rooster_imageButton00.getId()){
                buttonColors(rooster_imageButton00, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(0);
            } else if (v.getId() == rooster_imageButton01.getId()){
                buttonColors(rooster_imageButton01, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(1);
            } else if (v.getId() == rooster_imageButton10.getId()){
                buttonColors(rooster_imageButton10, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(2);
            } else if (v.getId() == rooster_imageButton11.getId()){
                buttonColors(rooster_imageButton11, getColor(R.color.blue), getColor(
                        R.color.light_blue));
                compareSequence(3);
            }
        }

    }

    /*The computer goes first and then it is the players turn, use a handler to
    to delay when the game starts */
    private void startGame() {

        textview_player.setText("SIMON TURN");
        player1=false;
        rooster_imageButton00.setEnabled(false);
        rooster_imageButton01.setEnabled(false);
        rooster_imageButton10.setEnabled(false);
        rooster_imageButton11.setEnabled(false);

        if(currentScore>highScore){
            highScore=currentScore;
            SharedPreferences hs= getSharedPreferences("GET_HIGH_SCORE_SURPRISE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hs.edit();
            editor.putInt("HIGH_SCORE_SURPRISE", highScore);
            editor.commit();
        }

        String csString = Integer.toString(currentScore);
        String hsString = Integer.toString(highScore);

        cs.setText(csString);
        hs.setText(hsString);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sequence();
            }
        }, 3000);
        currentScore++;
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
        if (x == 0) {
            playSound(rooster00);
            buttonColors(rooster_imageButton00, getColor(R.color.blue), getColor(
                    R.color.light_blue));
        } else if (x == 1) {
            playSound(rooster01);
            buttonColors(rooster_imageButton01, getColor(R.color.blue),
                    R.color.light_blue);
        } else if (x == 2) {
            playSound(rooster10);
            buttonColors(rooster_imageButton10, getColor(R.color.blue),
                    R.color.light_blue);
        } else if (x == 3) {
            playSound(rooster11);
            buttonColors(rooster_imageButton11, getColor(R.color.blue),
                    R.color.light_blue);
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
            rooster_imageButton00.setEnabled(true);
            rooster_imageButton01.setEnabled(true);
            rooster_imageButton10.setEnabled(true);
            rooster_imageButton11.setEnabled(true);
            textview_player.setText("PLAYER TURN");
            player1=true;
            selection=0;
        }

    }

    private void compareSequence (int x){
        if(x==sequence.get(selection)) {
            if (x == 0 && rooster_imageButton00.isSelected()){
                Log.i("CORRECT", "i");
            } else if (x == 1 && rooster_imageButton01.isSelected()) {
                Log.i("CORRECT", "i");
            } else if (x == 2 && rooster_imageButton10.isSelected()) {
                Log.i("CORRECT", "i");
            } else if (x == 3 && rooster_imageButton11.isSelected()) {
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
