package edu.apsu.simon;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class EliminationActivity extends AppCompatActivity {

    private SoundPool soundPool;

    private Set<Integer> soundsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimination);

        soundsLoaded = new HashSet<Integer>();

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
