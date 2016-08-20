package com.example.hp.media;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtTitle;
    private TextView txtTime;
    private SeekBar seekBar;
    private Button btnPlaay;
    private Button btnPause;
    private Button btnReset;
    //
    private MediaPlayer mediaPlayer;
    private android.os.Handler handler;
    // b
    private Boolean isReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
    }

    private void init() {
        txtTime = (TextView) findViewById(R.id.txt_time);
        txtTitle = (TextView) findViewById(R.id.txt_song);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        btnPause = (Button) findViewById(R.id.btn_Pause);
        btnPlaay = (Button) findViewById(R.id.btn_Play);
        btnReset = (Button) findViewById(R.id.btn_reset);
        mediaPlayer = MediaPlayer.create(this, R.raw.thuocvenhau);
        handler = new android.os.Handler();
        isReset = true;
        txtTitle.setText(getTitles());

    }

    private void addListener() {
        btnPlaay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private String getTitles(){
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.thuocvenhau);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this,uri);
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    private Runnable updteTime = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()){

                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                int currentTime= mediaPlayer.getCurrentPosition();
                txtTime.setText(String.format("%d: %d",currentTime/60000, (currentTime/1000)%60));
            }

            handler.postDelayed(this, 100);
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Play: {
                if (isReset) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.thuocvenhau);
                    isReset = false;
                }
                if (!mediaPlayer.isPlaying()){
                    btnPlaay.setText("Play");
                }
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
//                handler.postDelayed(updteTime, 100);
                break;
            }
            case R.id.btn_Pause: {
                btnPlaay.setText("Resume");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            }
            case R.id.btn_reset: {
                isReset = true;
                if (mediaPlayer != null) {
                    seekBar.setProgress(0);
                    mediaPlayer.reset();
                    handler.removeCallbacks(updteTime);
                }
                break;
            }
        }
    }
}
