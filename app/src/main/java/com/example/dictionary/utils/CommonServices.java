package com.example.dictionary.utils;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.dictionary.R;

import java.io.IOException;

public class CommonServices {

    private static MediaPlayer mediaPlayer;

    public static void playAudio(String audioUrl, android.content.Context context, ImageView playIcon) {
        // Release any existing MediaPlayer before starting a new one
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playIcon.setColorFilter(ContextCompat.getColor(context, R.color.holo_green_dark));

            // Release MediaPlayer when done
            mediaPlayer.setOnCompletionListener(mp -> {
                playIcon.setColorFilter(null);
                mp.release();
                mediaPlayer = null;
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
        }
    }

}
