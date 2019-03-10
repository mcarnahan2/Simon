package edu.apsu.simon;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class RewindActivity extends AppCompatActivity {
    private int[] ids = {
            R.id.blue_imageButton, R.id.green_imageButton,
            R.id.yellow_imageButton, R.id.red_imageButton
    };
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewind);

        ImageButton im;
        for(int id : ids){
            im = findViewById(id);
            im.setOnClickListener(new CheckButtonListener());
        }
    }

    class CheckButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

    class BackgroundTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

    class MainUIListener implements View.OnClickListener{
        MainUIListener(){

        }

        @Override
        public void onClick(View view) {
            
        }
    }
}
