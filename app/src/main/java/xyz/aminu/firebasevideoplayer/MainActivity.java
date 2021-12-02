package xyz.aminu.firebasevideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView   video_player;
    private ImageView   play_btn;
    private ProgressBar video_progress, buffer_progress;
    private TextView    video_duration;
    private Uri videoUri;
    private boolean isPlaying;
    private int video_current_time =   0;
    private int video_duration_time =   0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPlaying   =   false;

        video_player    =   findViewById(R.id.video_player);
        play_btn    =   findViewById(R.id.play_btn);
        video_progress  =   findViewById(R.id.video_progress);
        buffer_progress =   findViewById(R.id.buffer_progress);
        video_duration  =   findViewById(R.id.video_duration);

        videoUri    =   Uri.parse("https://firebasestorage.googleapis.com/v0/b/androidfirebase-362fa.appspot.com/o/T-Pain%20-%20Bartender%20(Official%20HD%20Video)%20ft.%20Akon.mp4?alt=media&token=09dc904e-8ca2-48e6-b2e1-03e0c81067d9");
        video_player.setVideoURI(videoUri);
        video_player.requestFocus();
        video_player.start();

        video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                video_duration_time =   video_player.getDuration()/1000;
                String  duration    =   String.format("%02d:%02d",  video_duration_time /   60, video_duration_time %   60);
                video_duration.setText(duration);

            }
        });

        new VideoProgress().execute();

        play_btn.setImageResource(R.drawable.ic_pause);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPlaying)  {

                    video_player.pause();
                    isPlaying   =   false;
                    play_btn.setImageResource(R.drawable.ic_play);

                } else {

                    video_player.start();
                    isPlaying   =   true;
                    play_btn.setImageResource(R.drawable.ic_pause);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        isPlaying   =   false;
    }

    public class VideoProgress  extends AsyncTask<Void, Integer,    Void>   {

        @Override
        protected Void  doInBackground(Void...  voids)  {

            do {

                if (isPlaying) {

                    video_current_time = video_player.getCurrentPosition() / 1000;
                    //int currentPercent  =   video_current_time  *   100/video_duration_time;
                    publishProgress(video_current_time);

                }

            }   while (video_progress.getProgress() <=  100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            try {

                int currentPercent  =   video_current_time  *   100/video_duration_time;
                video_progress.setProgress(values[0]);
                String  currentString   =   String.format("%02d:%02d",  values[0]   /   60, values[0]  %   60);
                video_duration.setText(currentString);

            }   catch (Exception    e)  {

            }
        }
    }
}